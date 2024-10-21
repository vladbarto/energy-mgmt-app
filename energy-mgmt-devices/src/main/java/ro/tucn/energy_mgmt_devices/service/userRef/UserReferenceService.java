package ro.tucn.energy_mgmt_devices.service.userRef;

import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserReferenceService {

    List<UserReferenceResponseDTO> findAll();
    UserReferenceResponseDTO save(UserReferenceRequestDTO userReferenceRequestDTO);
    UserReferenceResponseDTO update(UserReferenceRequestDTO userReferenceRequestDTO, UUID userReferenceId);
    UserReferenceResponseDTO deleteById(UUID userReferenceId);
}
