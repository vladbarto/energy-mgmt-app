package ro.tucn.energy_mgmt_devices.service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ro.tucn.energy_mgmt_devices.ampq.AmpqMessageSenderBase;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;
import ro.tucn.energy_mgmt_devices.util.MessageUtils;

@Slf4j
public class RabbitMqServiceBean
        extends AmpqMessageSenderBase<DeviceChangeRequestDTO>
        implements RabbitMqService {

    public RabbitMqServiceBean(String applicationName,
                               String queueName,
                               String exchangeName,
                               String routingKey,
                               RabbitTemplate rabbitTemplate,
                               ObjectMapper objectMapper
    ) {
        super(applicationName, queueName, exchangeName, routingKey, rabbitTemplate, objectMapper);
    }

    @Override
    public DeviceChangeResponseDTO transmitMessage(DeviceChangeRequestDTO deviceChangeRequestDTO) {
        SendingStatus status = sendMessage(deviceChangeRequestDTO);

        return MessageUtils.getDeviceChangeResponseDTO(deviceChangeRequestDTO, status);
    }
}
