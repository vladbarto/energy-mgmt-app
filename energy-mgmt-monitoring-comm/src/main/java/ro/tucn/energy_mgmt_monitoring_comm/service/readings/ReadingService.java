package ro.tucn.energy_mgmt_monitoring_comm.service.readings;

import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;

public interface ReadingService {

    ReadingResponseDTO save(ReadingRequestDTO readingRequestDTO);
}
