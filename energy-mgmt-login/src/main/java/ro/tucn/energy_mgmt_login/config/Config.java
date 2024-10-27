package ro.tucn.energy_mgmt_login.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_login.service.device.DeviceService;
import ro.tucn.energy_mgmt_login.service.device.DeviceServiceBean;
import ro.tucn.energy_mgmt_login.service.user.UserService;
import ro.tucn.energy_mgmt_login.service.user.UserServiceBean;

@Configuration
public class Config {
    @Bean
    public UserService userServiceBean (
            @Value("${microservices.userService}/user/v1/info") String url,
            RestTemplateBuilder restTemplateBuilder
    ) {
        return new UserServiceBean(url, restTemplateBuilder.build());
    }

    @Bean
    public DeviceService deviceServiceBean (
            @Value("${microservices.deviceService}/device/v1") String url,
            RestTemplateBuilder restTemplateBuilder
    ) {
        return new DeviceServiceBean(url, restTemplateBuilder.build());
    }

}
