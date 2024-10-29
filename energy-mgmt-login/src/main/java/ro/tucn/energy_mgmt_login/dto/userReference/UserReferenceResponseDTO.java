package ro.tucn.energy_mgmt_login.dto.userReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceResponseDTO {
    private UUID id;
    private UUID userId;
}
