package ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextMessageResponseDTO {
    private MessageType type;
    private String message;
    private UUID deviceId;
    private String date;
}
