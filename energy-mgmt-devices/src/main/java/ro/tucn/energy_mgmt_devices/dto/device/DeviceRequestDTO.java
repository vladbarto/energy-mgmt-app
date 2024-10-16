package ro.tucn.energy_mgmt_devices.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Necessary for user registration
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequestDTO {

    private String description;
    private String address;
    private float mhec;
    private UUID userId;
}
