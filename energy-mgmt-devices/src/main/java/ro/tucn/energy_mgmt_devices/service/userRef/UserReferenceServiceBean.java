package ro.tucn.energy_mgmt_devices.service.userRef;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_devices.exception.ExceptionCode;
import ro.tucn.energy_mgmt_devices.exception.NotFoundException;
import ro.tucn.energy_mgmt_devices.mapper.UserReferenceMapper;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;
import ro.tucn.energy_mgmt_devices.repository.UserReferenceRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserReferenceServiceBean implements UserReferenceService {

    private final UserReferenceRepository userReferenceRepository;
    private final UserReferenceMapper userReferenceMapper;
    private final String applicationName;

    @Override
    public List<UserReferenceResponseDTO> findAll() {
        log.info("Getting all userRefs for application {}", applicationName);

        List<UserReferenceEntity> userReferenceEntityList = userReferenceRepository.findAll();

        return userReferenceMapper.entityListToResponseDTOList(userReferenceEntityList);
    }

//    @Override
//    public UserReferenceResponseDTO findByUserReferenceName(String userReferencename) {
//        return userReferenceRepository.findByUserReferenceName(userReferencename)
//                .map(userReferenceMapper::entityToResponseDTO)
//                .orElseThrow(() -> new NotFoundException(String.format(
//                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), // TODO: create error code for userReference not found
//                        userReferencename
//                )));
//    }

    @Override
    @Transactional
    public UserReferenceResponseDTO save(UserReferenceRequestDTO userReferenceRequestDTO) {
        log.info("Posting a new userReference for application {}", applicationName);
        UserReferenceEntity userReferenceToBeAdded = userReferenceMapper.requestDTOToEntity(userReferenceRequestDTO);
        UserReferenceEntity userReferenceAdded = userReferenceRepository.save(userReferenceToBeAdded);

        return userReferenceMapper.entityToResponseDTO(userReferenceAdded);
    }

    @Override
    @Transactional
    public UserReferenceResponseDTO update(UserReferenceRequestDTO userReferenceRequestDTO, UUID userReferenceId) {
        log.info("Updating a userReference for application {}", applicationName);

        return userReferenceRepository.findById(userReferenceId)
                .map((userReferenceEntity)-> {
                    userReferenceEntity.setUserId(userReferenceRequestDTO.getUserId());

                    userReferenceRepository.save(userReferenceEntity);

                    return userReferenceEntity;
                })
                .map(userReferenceMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), //TODO: new error code, userReferencename to userReference
                        userReferenceId
                )))
                ;
    }

    @Override
    @Transactional
    public UserReferenceResponseDTO deleteById(UUID userReferenceId) {
        log.info("Deleting userReference with id {}", userReferenceId);
        userReferenceRepository.deleteById(userReferenceId);
        return null;
    }

}
