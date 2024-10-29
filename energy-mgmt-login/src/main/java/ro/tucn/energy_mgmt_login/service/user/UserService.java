package ro.tucn.energy_mgmt_login.service.user;

import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO getInfo(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> getAll();
    UserResponseDTO getByUsername(String username);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO deleteById(UUID id);
    UserResponseDTO updateById(UUID userId, UserRequestDTO userRequestDTO);
}
