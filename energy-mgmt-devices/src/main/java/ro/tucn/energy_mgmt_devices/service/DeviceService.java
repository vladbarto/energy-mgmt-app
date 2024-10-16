package ro.tucn.energy_mgmt_devices.service;

import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import java.util.List;
import java.util.UUID;

public interface DeviceService {

    List<DeviceResponseDTO> findAll();
    DeviceResponseDTO save(DeviceRequestDTO deviceRequestDTO);
    DeviceResponseDTO update(DeviceRequestDTO deviceRequestDTO, UUID deviceId);
    DeviceResponseDTO deleteById(UUID deviceId);
}
