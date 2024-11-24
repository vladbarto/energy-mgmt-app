package ro.tucn.energy_mgmt_monitoring_comm.service.readings;

import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ReadingService {

    List<ReadingResponseDTO> findAllByIdAndDate(UUID deviceId, String date);
    ReadingResponseDTO save(ReadingRequestDTO readingRequestDTO);
}
