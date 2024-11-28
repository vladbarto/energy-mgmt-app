package ro.tucn.energy_mgmt_devices.dto.deviceChange;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceChangeRequestDTO {
    private UUID deviceId;
    private UUID userId;
    private float mhec;
    private MethodType methodType;
}
