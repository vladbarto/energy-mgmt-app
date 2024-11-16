package ro.tucn.energy_mgmt_devices.config;

import lombok.RequiredArgsConstructor;
;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    @Value("${spring.application.name}")     public String applicationName;
    @Value("${spring.rabbitmq.queue}")       public String queueName;
    @Value("${spring.rabbitmq.exchange}")    public String exchangeName;
    @Value("${spring.rabbitmq.routing-key}") public String routingKey;

    @Bean
    public Queue queue() {
        log.info("[RabbitMQ] Creating queue {} for application {}", queueName, applicationName);
        return new Queue(queueName, false);
    }

    @Bean
    public TopicExchange exchange() {
        log.info("[RabbitMQ] Creating exchange {} for application {}", exchangeName, applicationName);
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        log.info("[RabbitMQ] Creating binding for queue {} and exchange {}; application {}", queue, exchange, applicationName);
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
