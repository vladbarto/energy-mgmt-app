package ro.tucn.energy_mgmt_devices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@SpringBootApplication
public class EnergyMgmtDevicesApplication {

	public static void main(String[] args) {

		SpringApplication.run(EnergyMgmtDevicesApplication.class, args);
	}

}
