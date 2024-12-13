package ro.tucn.energy_mgmt_chat.dto.wsAction;

import lombok.*;
import ro.tucn.energy_mgmt_chat.dto.message.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WsActionResponseDTO {
    private ActionType type;
    private UUID transmitter;
    private UUID receiver;
    private String text;
    private LocalDateTime sendingTime;
    private MessageStatus status;
    private UUID messageId;
}
