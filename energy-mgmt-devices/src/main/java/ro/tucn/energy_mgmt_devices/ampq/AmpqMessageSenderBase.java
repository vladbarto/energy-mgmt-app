package ro.tucn.energy_mgmt_devices.ampq;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;

@Slf4j
public abstract class AmpqMessageSenderBase<Request> implements MessageSender<Request> {

    protected final String applicationName;
    protected final String queueName;
    protected final String exchangeName;
    protected final String routingKey;
    protected final RabbitTemplate rabbitTemplate;
    protected final ObjectMapper objectMapper;

    protected AmpqMessageSenderBase(String applicationName, String queueName, String exchangeName, String routingKey, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.applicationName = applicationName;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public SendingStatus sendMessage(Request request) {
        log.info("Sending message <{}> to queue <{}> for application {}", request, queueName, applicationName);

        try {
            String payload = objectMapper.writeValueAsString(request);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, payload);

            return SendingStatus.SUCCESS;
        } catch (Exception e) {
            log.error("Failed to send message <{}> to queue <{}>", request, queueName, e);
        }

        return SendingStatus.FAILURE;
    }
}
