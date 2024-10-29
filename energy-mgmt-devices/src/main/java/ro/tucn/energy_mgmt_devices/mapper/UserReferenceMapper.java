package ro.tucn.energy_mgmt_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;

import java.util.List;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserReferenceMapper extends GenericMapper<UserReferenceEntity, UserReferenceRequestDTO, UserReferenceResponseDTO> {

}
