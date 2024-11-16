package ro.tucn.energy_mgmt_monitoring_comm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.model.DeviceEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper <DeviceEntity, DeviceRequestDTO, DeviceResponseDTO> {
}
