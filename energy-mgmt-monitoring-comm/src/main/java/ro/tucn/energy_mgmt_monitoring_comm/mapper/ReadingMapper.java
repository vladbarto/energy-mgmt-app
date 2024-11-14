package ro.tucn.energy_mgmt_monitoring_comm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.model.ReadingEntity;

/**
 * Mapper that handles conversion of energy counter readings between database
 * (as entity object) and Request DTO and Response DTO respectively.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReadingMapper extends GenericMapper<ReadingEntity, ReadingRequestDTO, ReadingResponseDTO> {
}
