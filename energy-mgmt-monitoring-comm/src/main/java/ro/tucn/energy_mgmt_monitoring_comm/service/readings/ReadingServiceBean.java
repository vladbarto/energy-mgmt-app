package ro.tucn.energy_mgmt_monitoring_comm.service.readings;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.mapper.ReadingMapper;
import ro.tucn.energy_mgmt_monitoring_comm.model.ReadingEntity;
import ro.tucn.energy_mgmt_monitoring_comm.repository.ReadingRepository;

@Slf4j
@RequiredArgsConstructor
public class ReadingServiceBean implements ReadingService {

    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;
    private final String applicationName;

    @Override
    @Transactional
    public ReadingResponseDTO save(ReadingRequestDTO readingRequestDTO) {
        log.info("save reading {} for application {}", readingRequestDTO, applicationName);

        ReadingEntity readingToBeSaved = readingMapper.requestDTOToEntity(readingRequestDTO);
        ReadingEntity readingSaved = readingRepository.save(readingToBeSaved);

        return readingMapper.entityToResponseDTO(readingSaved);
    }
}
