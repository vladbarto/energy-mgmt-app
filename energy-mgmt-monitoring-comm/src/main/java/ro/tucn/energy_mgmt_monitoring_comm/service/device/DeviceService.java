package ro.tucn.energy_mgmt_monitoring_comm.service.device;

import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceRequestDTO;

public interface DeviceService {

    void processMessage(DeviceRequestDTO deviceRequestDTO);
//    void save();
//    void update();
//    void delete();
}
