package ro.tucn.energy_mgmt_user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_user.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_user.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_user.exception.ExceptionCode;
import ro.tucn.energy_mgmt_user.exception.NotFoundException;
import ro.tucn.energy_mgmt_user.mapper.UserMapper;
import ro.tucn.energy_mgmt_user.model.UserEntity;
import ro.tucn.energy_mgmt_user.repository.UserRepository;
import ro.tucn.energy_mgmt_user.security.util.SecurityConstants;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserServiceBean implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final String applicationName;

    @Override
    public List<UserResponseDTO> findAll() {
        log.info("Getting all users for application {}", applicationName);

        List<UserEntity> userEntityList = userRepository.findAll();

        return userMapper.entityListToResponseDTOList(userEntityList);
    }

    @Override
    public UserResponseDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(),
                        username
                )));
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        UserEntity userToBeAdded = userMapper.requestDTOToEntity(userRequestDTO);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(SecurityConstants.PASSWORD_STRENGTH);
        userToBeAdded.setPassword(passwordEncoder.encode(userToBeAdded.getPassword()));//TODO

        UserEntity userAdded = userRepository.save(userToBeAdded);

        return userMapper.entityToResponseDTO(userAdded);
    }

    @Override
    @Transactional
    public UserResponseDTO update(UserRequestDTO userRequestDTO, UUID userId) {
        return userRepository.findById(userId)
                .map((userEntity)-> {
                    userEntity.setEmail(userRequestDTO.getEmail());
                    userEntity.setUsername(userRequestDTO.getUsername());
                    userEntity.setPassword(userRequestDTO.getPassword());

                    userRepository.save(userEntity);

                    return userEntity;
                })
                .map(userMapper::entityToResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_USERNAME_NOT_FOUND.getMessage(), //TODO: new error code, username to user
                        userId
                )))
                ;
    }

    @Override
    @Transactional
    public UserResponseDTO deleteById(UUID userId) {

        userRepository.deleteById(userId);
        return null;
    }
}
