package ro.tucn.energy_mgmt_monitoring_comm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.tucn.energy_mgmt_monitoring_comm.mapper.ReadingMapper;
import ro.tucn.energy_mgmt_monitoring_comm.repository.ReadingRepository;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingService;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingServiceBean;

@Configuration
public class Config {

    @Bean
    public ReadingService readingServiceBean(
            ReadingRepository readingRepository,
            ReadingMapper readingMapper,
            @Value("${spring.application.name}") String applicationName
    ) {
        return new ReadingServiceBean(readingRepository, readingMapper, applicationName);
    }
}