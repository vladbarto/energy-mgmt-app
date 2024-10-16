package ro.tucn.energy_mgmt_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper<DeviceEntity, DeviceRequestDTO, DeviceResponseDTO> {
}
