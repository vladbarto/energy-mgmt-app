package ro.tucn.energy_mgmt_login.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_login.resttemplate.RestTemplateBase;

import java.util.List;
import java.util.UUID;

@Slf4j
public class UserServiceBean
        extends RestTemplateBase<UserRequestDTO, UserResponseDTO>
        implements UserService {

    private final String url;

    public UserServiceBean(String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }

    @Override
    public Class<UserResponseDTO> getResponseType() {
        return UserResponseDTO.class;
    }


    @Override
    public String getExceptionMessage(UserRequestDTO userRequestDTO) {
        return String.format("%s ---> (FAIL)", userRequestDTO.getUsername());
    }

    @Override
    public UserResponseDTO getInfo(UserRequestDTO userRequestDTO) {

        String username = userRequestDTO.getUsername();
        log.info("Sent user info request {}", username);

        String url = String.format("%s/info?username=%s", this.url, username);

        return getForEntity(url, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        log.info("Sent user list request");

        String url = String.format("%s/all", this.url);

        return getForEntity(url, ParameterizedTypeReference.forType(List.class));
    }

    @Override
    public UserResponseDTO getByUsername(String username) {
        log.info("Sent user request {}", username);
        String url = String.format("%s/%s", this.url, username);
        return getForEntity(url, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        log.info("Sent insert user request {}", userRequestDTO.getUsername());

        String url = String.format("%s/one", this.url);
        return postForEntity(url, userRequestDTO);
    }

    @Override
    public UserResponseDTO deleteById(UUID id) {
        log.info("Sent user delete request {}", id);

        String url = String.format("%s/%s", this.url, id);

        return deleteForEntity(url, UserResponseDTO.class).getBody();
    }

    @Override
    public UserResponseDTO updateById(UUID id, UserRequestDTO userRequestDTO) {
        log.info("Sent user update request {}", id);

        String url = String.format("%s/%s", this.url, id);

        return putForEntity(url, userRequestDTO);
    }
}
