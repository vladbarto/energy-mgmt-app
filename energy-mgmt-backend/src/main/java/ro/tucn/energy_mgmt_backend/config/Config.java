package ro.tucn.energy_mgmt_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_backend.repository.UserRepository;
import ro.tucn.energy_mgmt_backend.mapper.UserMapper;
import ro.tucn.energy_mgmt_backend.service.UserService;
import ro.tucn.energy_mgmt_backend.service.UserServiceBean;

@Configuration
public class Config {

    @Bean
    public UserService userServiceBean(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        return new UserServiceBean(userRepository, userMapper);
    }
}
