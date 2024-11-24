package ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageRequestDTO {
    private MessageType type;
    private String message;
    private UUID deviceId;
    private String date;
    private float mhec;
}
