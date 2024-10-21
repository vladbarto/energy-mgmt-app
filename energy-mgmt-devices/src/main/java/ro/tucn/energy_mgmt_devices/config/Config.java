package ro.tucn.energy_mgmt_devices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_devices.mapper.UserReferenceMapper;
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;
import ro.tucn.energy_mgmt_devices.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_devices.repository.UserReferenceRepository;
import ro.tucn.energy_mgmt_devices.service.device.DeviceService;
import ro.tucn.energy_mgmt_devices.service.device.DeviceServiceBean;
import ro.tucn.energy_mgmt_devices.service.userRef.UserReferenceService;
import ro.tucn.energy_mgmt_devices.service.userRef.UserReferenceServiceBean;

@Configuration
public class Config {

    @Bean
    public DeviceService deviceServiceBean(
            DeviceRepository deviceRepository,
            DeviceMapper deviceMapper
    ) {
        return new DeviceServiceBean(deviceRepository, deviceMapper);
    }

    @Bean
    public UserReferenceService userReferenceServiceBean(
            UserReferenceRepository userReferenceRepository,
            UserReferenceMapper userReferenceMapper
    ) {
        return new UserReferenceServiceBean(userReferenceRepository, userReferenceMapper);
    }
}
