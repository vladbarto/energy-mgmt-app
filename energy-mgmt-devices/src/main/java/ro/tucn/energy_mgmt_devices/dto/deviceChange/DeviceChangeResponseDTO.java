package ro.tucn.energy_mgmt_devices.dto.deviceChange;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceChangeResponseDTO {
    private UUID deviceId;
    private SendingStatus status;
}
