package ro.tucn.energy_mgmt_login.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="\"USER\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Current class defines how a user should look like.
 * This class is compact and important for security.
 */
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ADMIN")
    private boolean isAdmin;
}

