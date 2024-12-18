package ro.tucn.energy_mgmt_devices.dto.userRef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceRequestDTO {

    private UUID userId;
}
