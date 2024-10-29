package ro.tucn.energy_mgmt_user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_user.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_user.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_user.model.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<UserEntity, UserRequestDTO, UserResponseDTO> {
}
