package ro.tucn.energy_mgmt_devices.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="DEVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Current class defines how a user should look like.
 * This class is compact and important for security.
 */
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "MHEC")
    private float mhec; /// maximum hourly energy consumption

    //@Column(name = "USERID")
    @ManyToOne()
    @JoinColumn(name="USERID", nullable=true)
    private UserReferenceEntity userId;
}
