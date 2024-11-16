package ro.tucn.energy_mgmt_devices.service.rabbitmq;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;

public interface RabbitMqService {

    DeviceChangeResponseDTO transmitMessage(DeviceChangeRequestDTO requestDTO);
}
