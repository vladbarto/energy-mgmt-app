package ro.tucn.energy_mgmt_monitoring_comm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "DEVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    private UUID deviceId;

    @Column(name = "USERID")
    private UUID userId;

    @Column(name = "MHEC")
    private float mhec; /// maximum hourly energy consumption
}
