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
    protected final RabbitTemplate rabbitTemplate;
    protected final ObjectMapper objectMapper;
    private final AmqpAdmin amqpAdmin;  // Added AmqpAdmin for declaration

    protected AmpqMessageSenderBase(String applicationName, String queueName, String exchangeName, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, ConnectionFactory connectionFactory) {
        this.applicationName = applicationName;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.amqpAdmin = new RabbitAdmin(connectionFactory);  // Initialize AmqpAdmin
    }

    // PostConstruct ensures exchange, queue, and binding are declared once the bean is created
    @PostConstruct
    private void setupQueueAndExchange() {
        Queue queue = queue();
        TopicExchange exchange = exchange();
        Binding binding = binding(queue, exchange);

        log.info("Declaring queue: {}", queueName);
        amqpAdmin.declareQueue(queue);

        log.info("Declaring exchange: {}", exchangeName);
        amqpAdmin.declareExchange(exchange);

        log.info("Declaring binding for queue {} and exchange {} with routing key 'routing.key.#'", queueName, exchangeName);
        amqpAdmin.declareBinding(binding);
    }

    @Override
    public Queue queue() {
        log.info("Creating queue {} for application {}", queueName, applicationName);
        return new Queue(queueName, false);
    }

    @Override
    public TopicExchange exchange() {
        log.info("Creating exchange {} for application {}", exchangeName, applicationName);
        return new TopicExchange(exchangeName);
    }

    @Override
    public Binding binding(Queue queue, TopicExchange exchange) {
        log.info("Creating binding for queue {} and exchange {}; application {}", queue, exchange, applicationName);
        return BindingBuilder.bind(queue).to(exchange).with("routing.key.#");
    }

    public SendingStatus sendMessage(Request request) {
        log.info("Sending message <{}> to queue <{}> for application {}", request, queueName, applicationName);

        try {
            String payload = objectMapper.writeValueAsString(request);
            rabbitTemplate.convertAndSend(exchangeName, "routing.key.name", payload);

            return SendingStatus.SUCCESS;
        } catch (Exception e) {
            log.error("Failed to send message <{}> to queue <{}>", request, queueName, e);
        }

        return SendingStatus.FAILURE;
    }
}
