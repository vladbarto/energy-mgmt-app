package ro.tucn.energy_mgmt_monitoring_comm.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="READING")
/**
 * Defines how an energy-meter-reading will be saved like in the database
 */
public class ReadingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="DEVICE_ID")
    private UUID deviceId;

    @Column(name="TIMESTAMP")
    private Timestamp timestamp;

    @Column(name="READ_VALUE")
    private float readValue;
}
