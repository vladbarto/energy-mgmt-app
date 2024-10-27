package ro.tucn.energy_mgmt_user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_user.repository.UserRepository;
import ro.tucn.energy_mgmt_user.mapper.UserMapper;
import ro.tucn.energy_mgmt_user.service.UserService;
import ro.tucn.energy_mgmt_user.service.UserServiceBean;

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
