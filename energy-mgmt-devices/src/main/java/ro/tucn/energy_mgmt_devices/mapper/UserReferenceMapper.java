package ro.tucn.energy_mgmt_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserReferenceMapper extends GenericMapper<UserReferenceEntity, UserReferenceRequestDTO, UserReferenceResponseDTO> {

}
