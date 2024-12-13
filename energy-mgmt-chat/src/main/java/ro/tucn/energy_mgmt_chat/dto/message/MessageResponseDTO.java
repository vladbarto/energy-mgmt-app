package ro.tucn.energy_mgmt_chat.dto.message;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageResponseDTO {
    private UUID id;
    private UUID transmitter;
    private UUID receiver;
    private String text;
    private LocalDateTime sendingTime;
    private MessageStatus status;
}