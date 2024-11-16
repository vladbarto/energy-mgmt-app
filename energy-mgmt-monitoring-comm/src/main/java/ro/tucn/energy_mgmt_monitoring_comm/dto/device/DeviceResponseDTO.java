package ro.tucn.energy_mgmt_monitoring_comm.dto.device;

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

    private UUID deviceId;
    private float mhec;
//    private MethodType methodType;
//    private SendingStatus status;
}
