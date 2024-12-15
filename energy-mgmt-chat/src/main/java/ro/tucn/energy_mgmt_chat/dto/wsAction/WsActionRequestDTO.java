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
/**
 * A.k.a. WebSocketAction Request DTO
 * incoming from client (frontend)
 * the user requests the backend to do some action (fetch some conversations)
 * or sends the backend some signal (e.g. I'm currently typing...)

 * Acts as a substitute component for REST HTTP Requests
 */
public class WsActionRequestDTO {
    private ActionType type;
    private String transmitter;
    private String receiver;
    private String text;
}
