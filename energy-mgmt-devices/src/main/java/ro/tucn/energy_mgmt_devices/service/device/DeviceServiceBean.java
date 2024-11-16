package ro.tucn.energy_mgmt_devices.service.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.MethodType;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;
import ro.tucn.energy_mgmt_devices.exception.ExceptionCode;
import ro.tucn.energy_mgmt_devices.exception.NotFoundException;
import ro.tucn.energy_mgmt_devices.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;
import ro.tucn.energy_mgmt_devices.repository.UserReferenceRepository;
import ro.tucn.energy_mgmt_devices.service.rabbitmq.RabbitMqService;
import ro.tucn.energy_mgmt_devices.service.rabbitmq.RabbitMqServiceBean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DeviceServiceBean implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final String applicationName;
    private final UserReferenceRepository userReferenceRepository;
    private final RabbitMqService rabbitMqServiceBean;
    @Override
    public List<DeviceResponseDTO> findAll() {
        log.info("Getting all devices for application {}", applicationName);

        List<DeviceEntity> deviceEntityList = deviceRepository.findAll();

        return deviceMapper.entityListToResponseDTOList(deviceEntityList);
    }

    @Override
    public List<DeviceResponseDTO> findAllBelongingToUserId(UUID userId) {
        log.info("Getting all devices belonging to user {} for application {}", userId, applicationName);

        // Filter the list of DeviceEntity by userId
        List<DeviceEntity> filteredDevices = deviceRepository.findAll()
                .stream()
                .filter(device -> device.getUserId().getUserId().equals(userId))
                .collect(Collectors.toList());

        // If the filtered list is empty, throw NotFoundException
        if (filteredDevices.isEmpty()) {
            throw new NotFoundException(String.format(
                    ExceptionCode.ERR003_DEVICE_OF_USERID_NOT_FOUND.getMessage(),
                    userId
            ));
        }

        // Map the list of DeviceEntity to DeviceResponseDTO
        return deviceMapper.entityListToResponseDTOList(filteredDevices);
    }

    @Override
    public List<DeviceResponseDTO> findAllByMhecGreaterThan(double mhec) {
        log.info("Getting all devices greater than mhec {} for application {}", mhec, applicationName);

        List<DeviceEntity> deviceEntityList = deviceRepository.findAllByMhecGreaterThan(mhec);
        return deviceMapper.entityListToResponseDTOList(deviceEntityList);
    }
//    @Override
//    public DeviceResponseDTO findByDeviceName(String devicename) {
//        return deviceRepository.findByDeviceName(devicename)
//                .map(deviceMapper::entityToResponseDTO)
//                .orElseThrow(() -> new NotFoundException(String.format(
//                        ExceptionCode.ERR004_DEVICE_NOT_FOUND.getMessage(),
//                        devicename
//                )));
//    }

    @Override
    @Transactional
    public DeviceResponseDTO save(DeviceRequestDTO deviceRequestDTO) {
        log.info("Posting a new device for application {}", applicationName);

        // Map the incoming DTO to the DeviceEntity
        DeviceEntity deviceToBeAdded = deviceMapper.requestDTOToEntity(deviceRequestDTO);

        // Assume the deviceRequestDTO contains the userId
        UUID userId = deviceRequestDTO.getUserId(); // Adjust this according to your DTO structure

        // Find or create the UserReferenceEntity
        UserReferenceEntity userReference = userReferenceRepository.findById(userId)
                .orElseGet(() -> {
                    // Create a new UserReferenceEntity if not found
                    UserReferenceEntity newUser = new UserReferenceEntity();
                    newUser.setUserId(userId);
                    return userReferenceRepository.save(newUser); // Persist the new user
                });

        // Set the user reference on the DeviceEntity
        deviceToBeAdded.setUserId(userReference);

        // Save the DeviceEntity
        DeviceEntity deviceAdded = deviceRepository.save(deviceToBeAdded);

        // Map the saved DeviceEntity back to a DTO for response
        DeviceResponseDTO response = deviceMapper.entityToResponseDTO(deviceAdded);

        // do synchronization with Monitoring Microservice
        SendingStatus sendingStatus = saveSync(deviceRequestDTO, response.getId());
        log.info("Sending status = {} for *adding* device {}", sendingStatus, deviceAdded);

        return response;
    }

    @Override
    @Transactional
    public DeviceResponseDTO update(DeviceRequestDTO deviceRequestDTO, UUID deviceId) {
        log.info("Updating device with id {} for application {}", deviceId, applicationName);

        // Update the device entity in the repository
        return deviceRepository.findById(deviceId)
                .map((deviceEntity) -> {
                    // Set updated fields
                    deviceEntity.setDescription(deviceRequestDTO.getDescription());
                    deviceEntity.setAddress(deviceRequestDTO.getAddress());
                    deviceEntity.setMhec(deviceRequestDTO.getMhec());

                    // Save the updated device entity
                    deviceRepository.save(deviceEntity);

                    // Call the updateSync method to sync with the monitoring microservice
                    SendingStatus syncStatus = updateSync(deviceRequestDTO, deviceId);

                    // Log the status of the synchronization
                    log.info("Sending status = {} for *updating* device {}", syncStatus, deviceId);

                    // Return the updated device entity as a response DTO
                    return deviceEntity;
                })
                .map(deviceMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), //TODO: new error code, devicename to device
                        deviceId
                )));
    }


    @Override
    @Transactional
    public DeviceResponseDTO deleteById(UUID deviceId) {
        log.info("Deleting device with id {} for application {}", deviceId, applicationName);

        try {
            deviceRepository.deleteById(deviceId);

            // Call the deleteSync method to sync with the monitoring microservice
            SendingStatus syncStatus = deleteSync(deviceId);

            // Log the status of the synchronization
            log.info("Sending status = {} for *deleting* device {}", syncStatus, deviceId);

            return null;

        } catch (NotFoundException e) {
            throw new NotFoundException(String.format(ExceptionCode.ERR004_DEVICE_NOT_FOUND.getMessage(), deviceId));
        }
    }

    private SendingStatus saveSync(DeviceRequestDTO deviceRequestDTO, UUID deviceId) {
        log.info("Saving device <{}> in Monitoring microservice <energy-mgmt-monitoring-comm>", deviceRequestDTO.getDescription());

        DeviceChangeRequestDTO requestDTO = DeviceChangeRequestDTO.builder()
                .deviceId(deviceId)
                .mhec(deviceRequestDTO.getMhec())
                .methodType(MethodType.POST)
                .build();
        DeviceChangeResponseDTO result = rabbitMqServiceBean.transmitMessage(requestDTO);

        return result.getStatus();
    }

    private SendingStatus updateSync(DeviceRequestDTO deviceRequestDTO, UUID deviceId) {
        log.info("Updating device <{}> in Monitoring microservice <energy-mgmt-monitoring-comm>", deviceRequestDTO.getDescription());

        DeviceChangeRequestDTO requestDTO = DeviceChangeRequestDTO.builder()
                .deviceId(deviceId)
                .mhec(deviceRequestDTO.getMhec())
                .methodType(MethodType.UPDATE)
                .build();
        DeviceChangeResponseDTO result = rabbitMqServiceBean.transmitMessage(requestDTO);

        return result.getStatus();
    }

    private SendingStatus deleteSync(UUID deviceId) {
        log.info("Deleting device with id {} in Monitoring microservice <energy-mgmt-monitoring-comm>", deviceId);

        DeviceChangeRequestDTO requestDTO = DeviceChangeRequestDTO.builder()
                .deviceId(deviceId)
                .methodType(MethodType.DELETE)
                .build();
        DeviceChangeResponseDTO result = rabbitMqServiceBean.transmitMessage(requestDTO);

        return result.getStatus();
    }

}
