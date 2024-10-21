package ro.tucn.energy_mgmt_devices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="USERREFERENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceEntity {

    @OneToMany

    private List<DeviceEntity> id;

    @Id
    @Column(name="USERID")
    private UUID userId;
}
