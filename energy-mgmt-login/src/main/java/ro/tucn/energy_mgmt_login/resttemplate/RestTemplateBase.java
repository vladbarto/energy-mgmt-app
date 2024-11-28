package ro.tucn.energy_mgmt_login.resttemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.exception.RestTemplateException;

@Slf4j
@RequiredArgsConstructor
public abstract class RestTemplateBase<Request, Response> {

    private final RestTemplate restTemplate;

    public Response getForEntity(String url, Class<Response> responseClass) {
        try {
            ResponseEntity<Response> responseEntity = restTemplate.getForEntity(url, responseClass, getResponseType());

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RestTemplateException("Error fetching data from GET request to URL: " + url);
            }

            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new RestTemplateException("Error fetching data from GET request to URL: " + url);
        }
    }

    // New method for List<ResponseDTO> or other complex types
    public <T> T getForEntity(String url, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RestTemplateException("[Not a 2xx code] Error fetching data from GET request to URL: " + url);
            }

            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new RestTemplateException("Error fetching data from GET request to URL: " + url);
        }
    }

    public Response postForEntity(String url, Request request) {
        HttpEntity<Request> requestEntity = buildRequestEntity(request);

        try {
            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, requestEntity, getResponseType());

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("postForEntity request fails");
                throw new RestTemplateException(getExceptionMessage(request));
            }

            return responseEntity.getBody();
        } catch (RestClientException e) {
            log.info("postForEntity request fails for unknown reason");
            throw new RestTemplateException(getExceptionMessage(request));
        }
    }

    public ResponseEntity<Response> deleteForEntity(String url, Class<Response> responseClass) {
        try {
            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    responseClass
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("deleteForEntity request failed");
                throw new RestTemplateException("Error deleting data from DELETE request to URL: " + url);
            }

            return responseEntity;
        } catch (RestClientException e) {
            log.info("deleteForEntity request failed due to unknown reason");
            throw new RestTemplateException("Error deleting data from DELETE request to URL: " + url);
        }
    }

    public Response putForEntity(String url, Request request) {
        HttpEntity<Request> requestEntity = buildRequestEntity(request);

        try {
            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    getResponseType());


            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("putForEntity request fails");
                throw new RestTemplateException(getExceptionMessage(request));
            }
            return responseEntity.getBody();

        } catch(RestClientException e) {
            log.info("putForEntity request fails for unknown reason");
            throw new RestTemplateException(getExceptionMessage(request));
        }
    }

    public abstract Class<Response> getResponseType();

    public abstract String getExceptionMessage(Request request);

    private HttpEntity<Request> buildRequestEntity(Request request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, headers);
    }
}
