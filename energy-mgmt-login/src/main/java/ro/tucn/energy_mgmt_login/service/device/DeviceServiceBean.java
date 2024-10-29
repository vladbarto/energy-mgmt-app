package ro.tucn.energy_mgmt_login.service.device;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_login.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_login.resttemplate.RestTemplateBase;

import java.util.List;
import java.util.UUID;

@Slf4j
public class DeviceServiceBean
    extends RestTemplateBase<DeviceRequestDTO, DeviceResponseDTO>
    implements DeviceService
{

    private final String url;


    public DeviceServiceBean(String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }

    @Override
    public Class<DeviceResponseDTO> getResponseType() {
        return DeviceResponseDTO.class;
    }

    @Override
    public String getExceptionMessage(DeviceRequestDTO deviceRequestDTO) {
        return String.format("%s ---> (FAIL)", deviceRequestDTO.getDescription());
    }

    @Override
    public DeviceResponseDTO saveDevice(DeviceRequestDTO deviceRequestDTO) {
        log.info("Sent add-to-database request: {}", deviceRequestDTO.toString());

        String url = String.format("%s/one", this.url);
        return postForEntity(url, deviceRequestDTO);
    }

    @Override
    public List<DeviceResponseDTO> findAll() {
        log.info("Sent findAll request");

        String url = String.format("%s/all", this.url);
        return getForEntity(url, ParameterizedTypeReference.forType(List.class));
    }
    @Override
    public List<DeviceResponseDTO> findAllByUserId(UUID userId) {
        log.info("Retrieving all devices of user {}", userId);

        String url = String.format("%s/user?id=%s", this.url, userId.toString());
        log.info("Se face requestul {}", url);
        return getForEntity(url, ParameterizedTypeReference.forType(List.class));
    }

    @Override
    public List<DeviceResponseDTO> findAllByMhecGreaterThan(double mhec) {
        log.info("Retrieving all devices of mhec greater than {}", mhec);

        String url = String.format("%s/all/%s", this.url, mhec);

        return getForEntity(url, ParameterizedTypeReference.forType(List.class));
    }

    @Override
    public DeviceResponseDTO updateById(UUID id, DeviceRequestDTO deviceRequestDTO) {
        log.info("Sent device update request {}", id);

        String url = String.format("%s/%s", this.url, id);

        return putForEntity(url, deviceRequestDTO);
    }

    @Override
    public DeviceResponseDTO deleteById(UUID id) {
        log.info("Sent device delete request {}", id);

        String url = String.format("%s/%s", this.url, id);

        return deleteForEntity(url, DeviceResponseDTO.class).getBody();
    }
}
