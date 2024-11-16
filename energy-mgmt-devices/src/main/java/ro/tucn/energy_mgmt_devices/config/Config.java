package ro.tucn.energy_mgmt_devices.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_devices.mapper.UserReferenceMapper;
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;
import ro.tucn.energy_mgmt_devices.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_devices.repository.UserReferenceRepository;
import ro.tucn.energy_mgmt_devices.service.device.DeviceService;
import ro.tucn.energy_mgmt_devices.service.device.DeviceServiceBean;
import ro.tucn.energy_mgmt_devices.service.rabbitmq.RabbitMqService;
import ro.tucn.energy_mgmt_devices.service.rabbitmq.RabbitMqServiceBean;
import ro.tucn.energy_mgmt_devices.service.userRef.UserReferenceService;
import ro.tucn.energy_mgmt_devices.service.userRef.UserReferenceServiceBean;

@Configuration
public class Config {

    @Bean
    public DeviceService deviceServiceBean(
            DeviceRepository deviceRepository,
            DeviceMapper deviceMapper,
            @Value("${spring.application.name:BACKEND}") String applicationName,
            UserReferenceRepository userReferenceRepository,
            RabbitMqService rabbitMqServiceBean
    ) {
        return new DeviceServiceBean(deviceRepository, deviceMapper, applicationName, userReferenceRepository, rabbitMqServiceBean);
    }

    @Bean
    public UserReferenceService userReferenceServiceBean(
            UserReferenceRepository userReferenceRepository,
            UserReferenceMapper userReferenceMapper,
            @Value("${spring.application.name:BACKEND}") String applicationName
    ) {
        return new UserReferenceServiceBean(userReferenceRepository, userReferenceMapper, applicationName);
    }

    @Bean
    public RabbitMqService rabbitMqServiceBean(
            @Value("${spring.application.name:BACKEND}") String applicationName,
            @Value("${spring.rabbitmq.queue}") String queueName,
            @Value("${spring.rabbitmq.exchange}") String exchangeName,
            @Value("${spring.rabbitmq.routing-key}") String routingKey,
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper
    ) {
        return new RabbitMqServiceBean(applicationName, queueName, exchangeName, routingKey, rabbitTemplate, objectMapper);
    }

}
