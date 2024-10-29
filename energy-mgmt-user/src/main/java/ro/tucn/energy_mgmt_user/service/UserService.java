package ro.tucn.energy_mgmt_user.service;

import ro.tucn.energy_mgmt_user.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_user.dto.user.UserRequestDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponseDTO> findAll();
    UserResponseDTO findByUsername(String username);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO update(UserRequestDTO userRequestDTO, UUID userId);
    UserResponseDTO deleteById(UUID userId);
    //UserResponseDTO deleteOwn();
}
