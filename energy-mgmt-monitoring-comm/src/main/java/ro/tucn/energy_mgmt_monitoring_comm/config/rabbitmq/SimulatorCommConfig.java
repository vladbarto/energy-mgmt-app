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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimulatorCommConfig {

    @Value("${spring.rabbitmq.simulator.queue}")        private String simulatorQueueName;
    @Value("${spring.rabbitmq.simulator.exchange}")     private String simulatorExchangeName;
    @Value("${spring.rabbitmq.simulator.host}")         private String simulatorHost;
    @Value("${spring.rabbitmq.simulator.port}")         private int    simulatorPort;
    @Value("${spring.rabbitmq.simulator.username}")     private String simulatorUsername;
    @Value("${spring.rabbitmq.simulator.password}")     private String simulatorPassword;
    @Value("${spring.rabbitmq.simulator.virtual-host}") private String simulatorVirtualHost;
    @Value("${spring.rabbitmq.simulator.routing-key}")  private String simulatorRoutingKey;

    @Bean(name = "simulatorConnectionFactory")
    public ConnectionFactory simulatorConnectionFactory() {
        log.info("Setting up RabbitMQ ConnectionFactory for 'simulator' with host: {}", simulatorHost);
        CachingConnectionFactory factory = new CachingConnectionFactory(simulatorHost, simulatorPort);
        factory.setUsername(simulatorUsername);
        factory.setPassword(simulatorPassword);
        factory.setVirtualHost(simulatorVirtualHost);
        return factory;
    }

    @Bean(name = "simulatorRabbitTemplate")
    public RabbitTemplate simulatorRabbitTemplate(@Qualifier("simulatorConnectionFactory") ConnectionFactory simulatorConnectionFactory) {
        return new RabbitTemplate(simulatorConnectionFactory);
    }

    @Bean
    public Queue simulatorQueue() {
        return new Queue(simulatorQueueName, false);
    }

    @Bean
    public DirectExchange simulatorExchange() {
        return new DirectExchange(simulatorExchangeName);
    }

    @Bean
    public Binding simulatorBinding(Queue simulatorQueue, DirectExchange simulatorExchange) {
        return BindingBuilder.bind(simulatorQueue).to(simulatorExchange).with(simulatorRoutingKey);
    }

    @Bean(name = "simulatorListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simulatorListenerContainerFactory(
            @Qualifier("simulatorConnectionFactory") ConnectionFactory simulatorConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(simulatorConnectionFactory); // This sets the connection factory for the listener container
        return factory;
    }


}
