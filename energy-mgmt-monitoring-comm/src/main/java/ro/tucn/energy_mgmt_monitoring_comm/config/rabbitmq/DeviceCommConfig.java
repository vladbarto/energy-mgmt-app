package ro.tucn.energy_mgmt_monitoring_comm.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeviceCommConfig {

    @Value("${spring.rabbitmq.from-device.queue}")          private String deviceQueueName;
    @Value("${spring.rabbitmq.from-device.exchange}")       private String deviceExchangeName;
    @Value("${spring.rabbitmq.from-device.host}")           private String deviceHost;
    @Value("${spring.rabbitmq.from-device.port}")           private int    devicePort;
    @Value("${spring.rabbitmq.from-device.username}")       private String deviceUsername;
    @Value("${spring.rabbitmq.from-device.password}")       private String devicePassword;
    @Value("${spring.rabbitmq.from-device.virtual-host}")   private String deviceVirtualHost;
    @Value("${spring.rabbitmq.from-device.routing-key}")    private String deviceRoutingKey;

    @Bean(name = "deviceConnectionFactory")
    @Primary
    public ConnectionFactory deviceConnectionFactory() {
        log.info("Setting up RabbitMQ ConnectionFactory for 'from-device' with host: {}", deviceHost);
        CachingConnectionFactory factory = new CachingConnectionFactory(deviceHost, devicePort);
        factory.setUsername(deviceUsername);
        factory.setPassword(devicePassword);
        factory.setVirtualHost(deviceVirtualHost);
        return factory;
    }

    @Bean(name = "deviceRabbitTemplate")
    public RabbitTemplate deviceRabbitTemplate(@Qualifier("deviceConnectionFactory") ConnectionFactory deviceConnectionFactory) {
        return new RabbitTemplate(deviceConnectionFactory);
    }

    @Bean
    public Queue deviceQueue() {
        return new Queue(deviceQueueName, false);
    }

    @Bean
    public TopicExchange deviceExchange() {
        return new TopicExchange(deviceExchangeName);
    }

    @Bean
    public Binding deviceBinding(Queue deviceQueue, TopicExchange deviceExchange) {
        return BindingBuilder.bind(deviceQueue).to(deviceExchange).with(deviceRoutingKey);
    }

    @Bean(name = "deviceRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory deviceRabbitListenerContainerFactory(
            @Qualifier("deviceConnectionFactory") ConnectionFactory deviceConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(deviceConnectionFactory);
        return factory;
    }

}
