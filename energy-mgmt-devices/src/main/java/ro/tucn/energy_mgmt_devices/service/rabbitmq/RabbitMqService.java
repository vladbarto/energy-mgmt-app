package ro.tucn.energy_mgmt_devices.service.rabbitmq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;

public interface RabbitMqService {

    DeviceChangeResponseDTO transmitMessage(DeviceChangeRequestDTO requestDTO);
}
