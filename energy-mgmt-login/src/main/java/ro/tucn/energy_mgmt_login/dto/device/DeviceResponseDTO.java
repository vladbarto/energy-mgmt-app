package ro.tucn.energy_mgmt_login.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {

    private UUID id;
    private String description;
    private String address;
    private float mhec;
    private UUID userId;
}
