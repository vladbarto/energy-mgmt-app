package ro.tucn.energy_mgmt_monitoring_comm.service.device;

import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;

import java.util.UUID;

public interface DeviceService {

    DeviceResponseDTO findDevice(UUID deviceId);
    void processMessage(DeviceRequestDTO deviceRequestDTO);
//    void save();
//    void update();
//    void delete();
}
