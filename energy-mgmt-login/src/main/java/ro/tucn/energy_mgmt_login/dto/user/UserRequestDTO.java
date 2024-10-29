package ro.tucn.energy_mgmt_login.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String email;
    private String username;
    private String password;
    private boolean isAdmin;
}
