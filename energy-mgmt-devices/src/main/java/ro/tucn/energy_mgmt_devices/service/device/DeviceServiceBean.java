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
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class DeviceServiceBean implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public List<DeviceResponseDTO> findAll() {
        log.info("Getting all chefs for application {}", "Application name TODO Fill");

        List<DeviceEntity> deviceEntityList = deviceRepository.findAll();

        return deviceMapper.entityListToResponseDTOList(deviceEntityList);
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

    @Override
    @Transactional
    public DeviceResponseDTO save(DeviceRequestDTO deviceRequestDTO) {
        log.info("Posting a new device for application {}", "App name TODO Fill");
        DeviceEntity deviceToBeAdded = deviceMapper.requestDTOToEntity(deviceRequestDTO);
        DeviceEntity deviceAdded = deviceRepository.save(deviceToBeAdded);
        
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
