package ro.tucn.energy_mgmt_login.service.device;

import ro.tucn.energy_mgmt_login.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_login.dto.device.DeviceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    DeviceResponseDTO saveDevice(DeviceRequestDTO deviceRequestDTO);
    List<DeviceResponseDTO> findAllByUserId(UUID userId);
    List<DeviceResponseDTO> findAll();
    List<DeviceResponseDTO> findAllByMhecGreaterThan(double rating);
    DeviceResponseDTO updateById(UUID id, DeviceRequestDTO deviceRequestDTO);
    DeviceResponseDTO deleteById(UUID id);
}
