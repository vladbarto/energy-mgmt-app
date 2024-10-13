package ro.tucn.energy_mgmt_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_backend.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_backend.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_backend.model.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<UserEntity, UserRequestDTO, UserResponseDTO> {
}
