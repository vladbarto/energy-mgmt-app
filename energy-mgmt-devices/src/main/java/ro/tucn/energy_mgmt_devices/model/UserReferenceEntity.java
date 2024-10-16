package ro.tucn.energy_mgmt_devices.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="USER_REFERENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceEntity {
    @Id
    private UUID id;

    @Column(name="USER_ID")
    private UUID userId;
}
