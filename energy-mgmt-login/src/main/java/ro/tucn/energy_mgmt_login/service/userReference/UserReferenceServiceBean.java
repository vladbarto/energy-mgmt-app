package ro.tucn.energy_mgmt_login.service.userReference;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.dto.userReference.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_login.dto.userReference.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_login.resttemplate.RestTemplateBase;

import java.util.UUID;

@Slf4j
public class UserReferenceServiceBean
    extends RestTemplateBase<UserReferenceRequestDTO, UserReferenceResponseDTO>
    implements UserReferenceService {

    private final String url;

    public UserReferenceServiceBean(String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }

    @Override
    public Class<UserReferenceResponseDTO> getResponseType() {
        return UserReferenceResponseDTO.class;
    }

    @Override
    public String getExceptionMessage(UserReferenceRequestDTO userReferenceRequestDTO) {
        return String.format("%s ---> (FAIL)", userReferenceRequestDTO.getUserId());
    }

    @Override
    public UserReferenceResponseDTO delete(UUID id) {
        log.info("Sent userReference delete request {}", id);

        String url = String.format("%s/%s", this.url, id);

        return deleteForEntity(url, UserReferenceResponseDTO.class).getBody();
    }

}
