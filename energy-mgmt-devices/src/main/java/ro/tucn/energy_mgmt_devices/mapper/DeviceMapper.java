package ro.tucn.energy_mgmt_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper<DeviceEntity, DeviceRequestDTO, DeviceResponseDTO> {

    // Custom mapping method to extract UUID from UserReferenceEntity
    default UUID map(UserReferenceEntity userReferenceEntity) {
        return userReferenceEntity != null ? userReferenceEntity.getUserId() : null;
    }

    // Custom mapping method to create UserReferenceEntity from UUID
    default UserReferenceEntity map(UUID userId) {
        if (userId == null) {
            return null;
        }
        UserReferenceEntity userReferenceEntity = new UserReferenceEntity();
        userReferenceEntity.setUserId(userId);
        return userReferenceEntity;
    }
}
