package ro.tucn.energy_mgmt_login.service.user;

import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;

public interface UserService {
    UserResponseDTO getInfo(UserRequestDTO userRequestDTO);

}
