package ro.tucn.energy_mgmt_login.service.userReference;

import ro.tucn.energy_mgmt_login.dto.userReference.UserReferenceResponseDTO;

import java.util.UUID;

public interface UserReferenceService {

    UserReferenceResponseDTO delete(UUID id);
}
