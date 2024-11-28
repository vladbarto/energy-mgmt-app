package ro.tucn.energy_mgmt_monitoring_comm.dto.device;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceResponseDTO {

    private UUID deviceId;
    private UUID userId;
    private float mhec;
}
