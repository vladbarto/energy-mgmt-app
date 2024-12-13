package ro.tucn.energy_mgmt_chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tucn.energy_mgmt_chat.dto.message.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="MESSAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "TRANSMITTER")
    private UUID transmitter;

    @Column(name = "RECEIVER")
    private UUID receiver;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "SENDINGTIME")
    private LocalDateTime sendingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private MessageStatus Status;
}
