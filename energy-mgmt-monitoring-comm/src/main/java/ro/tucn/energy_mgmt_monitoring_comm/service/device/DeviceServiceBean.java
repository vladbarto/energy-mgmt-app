package ro.tucn.energy_mgmt_monitoring_comm.service.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.exception.ExceptionCode;
import ro.tucn.energy_mgmt_monitoring_comm.exception.NotFoundException;
import ro.tucn.energy_mgmt_monitoring_comm.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_monitoring_comm.model.DeviceEntity;
import ro.tucn.energy_mgmt_monitoring_comm.repository.DeviceRepository;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class DeviceServiceBean implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final String applicationName;

    @Override
    public DeviceResponseDTO findDevice(UUID deviceId) {
        log.info("Device with id {}", deviceId);

        return deviceRepository.findById(deviceId)
                .map(deviceMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR004_DEVICE_NOT_FOUND.getMessage(),
                        deviceId
                )));
    }

    @Transactional
    protected void save(DeviceRequestDTO deviceRequestDTO) {
        log.info("Posting a new device for application {}", applicationName);

        // Map the incoming DTO to the DeviceEntity
        DeviceEntity deviceToBeAdded = deviceMapper.requestDTOToEntity(deviceRequestDTO);

        // Save the DeviceEntity
        DeviceEntity deviceAdded = deviceRepository.save(deviceToBeAdded);
    }

    @Transactional
    protected void update(DeviceRequestDTO deviceRequestDTO) {
        UUID deviceId = deviceRequestDTO.getDeviceId();
        log.info("Updating device with id {} for application {}", deviceId, applicationName);

        // Update the device entity in the repository
        deviceRepository.findById(deviceId)
                .map((deviceEntity) -> {
                    // Set updated fields
                    deviceEntity.setMhec(deviceRequestDTO.getMhec());

                    // Save the updated device entity
                    deviceRepository.save(deviceEntity);
                    return deviceEntity;
                })
                .map(deviceMapper::entityToResponseDTO) // not really necessary
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR004_DEVICE_NOT_FOUND.getMessage(),
                        deviceId
                )));
    }

    @Transactional
    protected void delete(DeviceRequestDTO deviceRequestDTO) {
        UUID deviceId = deviceRequestDTO.getDeviceId();

        log.info("Deleting device with id {} for application {}", deviceId, applicationName);

        try {
            deviceRepository.deleteById(deviceId);
        } catch (NotFoundException e) {
            throw new NotFoundException(String.format(ExceptionCode.ERR004_DEVICE_NOT_FOUND.getMessage(), deviceId));
        }
    }

    @Override
    public void processMessage(DeviceRequestDTO deviceRequestDTO) {
        switch(deviceRequestDTO.getMethodType()) {
            case POST -> save(deviceRequestDTO);
            case UPDATE -> update(deviceRequestDTO);
            case DELETE -> delete(deviceRequestDTO);
        }
    }
}
