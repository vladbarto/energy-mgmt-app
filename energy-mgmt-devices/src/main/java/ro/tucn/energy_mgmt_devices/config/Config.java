package ro.tucn.energy_mgmt_devices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_devices.repository.DeviceRepository;
import ro.tucn.energy_mgmt_devices.mapper.DeviceMapper;
import ro.tucn.energy_mgmt_devices.service.DeviceService;
import ro.tucn.energy_mgmt_devices.service.DeviceServiceBean;

@Configuration
public class Config {

    @Bean
    public DeviceService deviceServiceBean(
            DeviceRepository deviceRepository,
            DeviceMapper deviceMapper
    ) {
        return new DeviceServiceBean(deviceRepository, deviceMapper);
    }
}
