package ro.tucn.energy_mgmt_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@SpringBootApplication
public class EnergyMgmtBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(EnergyMgmtBackendApplication.class, args);
	}

}
