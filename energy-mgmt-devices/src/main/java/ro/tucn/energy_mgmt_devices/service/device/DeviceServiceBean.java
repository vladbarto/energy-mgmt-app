package ro.tucn.energy_mgmt_devices.service.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.exception.ExceptionCode;
import ro.tucn.energy_mgmt_devices.exception.NotFoundException;
import ro.tucn.energy_mgmt_devices.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;
import ro.tucn.energy_mgmt_devices.repository.UserReferenceRepository;

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
//    @Override
//    public DeviceResponseDTO findByDeviceName(String devicename) {
//        return deviceRepository.findByDeviceName(devicename)
//                .map(deviceMapper::entityToResponseDTO)
//                .orElseThrow(() -> new NotFoundException(String.format(
//                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), // TODO: create error code for device not found
//                        devicename
//                )));
//    }

//    @Override
//    @Transactional
//    public DeviceResponseDTO save(DeviceRequestDTO deviceRequestDTO) {
//        log.info("Posting a new device for application {}", applicationName);
//        DeviceEntity deviceToBeAdded = deviceMapper.requestDTOToEntity(deviceRequestDTO);
//        DeviceEntity deviceAdded = deviceRepository.save(deviceToBeAdded);
//
//        return deviceMapper.entityToResponseDTO(deviceAdded);
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
        return deviceMapper.entityToResponseDTO(deviceAdded);
    }

    @Override
    @Transactional
    public DeviceResponseDTO update(DeviceRequestDTO deviceRequestDTO, UUID deviceId) {
        return deviceRepository.findById(deviceId)
                .map((deviceEntity)-> {
                    deviceEntity.setDescription(deviceRequestDTO.getDescription());
                    deviceEntity.setAddress(deviceRequestDTO.getAddress());
                    deviceEntity.setMhec(deviceRequestDTO.getMhec());

                    deviceRepository.save(deviceEntity);

                    return deviceEntity;
                })
                .map(deviceMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), //TODO: new error code, devicename to device
                        deviceId
                )))
                ;
    }

    @Override
    @Transactional
    public DeviceResponseDTO deleteById(UUID deviceId) {

        deviceRepository.deleteById(deviceId);
        return null;
    }

}
