package ro.tucn.energy_mgmt_monitoring_comm.service.readings;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.exception.NotFoundException;
import ro.tucn.energy_mgmt_monitoring_comm.mapper.ReadingMapper;
import ro.tucn.energy_mgmt_monitoring_comm.model.ReadingEntity;
import ro.tucn.energy_mgmt_monitoring_comm.repository.ReadingRepository;
import ro.tucn.energy_mgmt_monitoring_comm.exception.ExceptionCode;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ReadingServiceBean implements ReadingService {

    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;
    private final String applicationName;

    @Override
    public List<ReadingResponseDTO> findAllByIdAndDate(UUID deviceId, String date) {
        log.info("Find all readings by id: {}, {}", deviceId, date);

        // Parse the input date string
        LocalDate givenDate = LocalDate.parse(date); // Example: "2024-11-23"

        // Create Timestamps for the start and end of the day
        Timestamp startOfDay = Timestamp.valueOf(givenDate.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(givenDate.atTime(LocalTime.MAX));

        List<ReadingEntity> readingEntityList = readingRepository.findAllByDeviceIdAndTimestampBetween(
                deviceId, startOfDay, endOfDay);

        return readingMapper.entityListToResponseDTOList(readingEntityList);
    }

    @Override
    @Transactional
    public ReadingResponseDTO save(ReadingRequestDTO readingRequestDTO) {
        log.info("save reading {} for application {}", readingRequestDTO, applicationName);

        ReadingEntity readingToBeSaved = readingMapper.requestDTOToEntity(readingRequestDTO);
        ReadingEntity readingSaved = readingRepository.save(readingToBeSaved);

        return readingMapper.entityToResponseDTO(readingSaved);
    }
}
