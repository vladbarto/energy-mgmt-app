package ro.tucn.energy_mgmt_backend.service;

import ro.tucn.energy_mgmt_backend.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_backend.dto.user.UserRequestDTO;
import java.util.List;

public interface UserService {

    List<UserResponseDTO> findAll();
    UserResponseDTO findByUsername(String username);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
}
