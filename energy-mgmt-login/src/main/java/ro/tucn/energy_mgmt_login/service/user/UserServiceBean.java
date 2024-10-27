package ro.tucn.energy_mgmt_login.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_login.resttemplate.RestTemplateBase;

@Slf4j
public class UserServiceBean
        extends RestTemplateBase<UserRequestDTO, UserResponseDTO>
        implements UserService {

    private final String url;

    @Value("${microservices.userService}")
    private String userServiceUrl;

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

        String url = String.format("%s/user/v1/info?username=%s", userServiceUrl, username);

        return getForEntity(url, UserResponseDTO.class);
    }
}
