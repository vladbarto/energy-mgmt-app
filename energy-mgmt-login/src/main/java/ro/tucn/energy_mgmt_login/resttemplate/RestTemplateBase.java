package ro.tucn.energy_mgmt_login.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ro.tucn.energy_mgmt_login.exception.RestTemplateException;

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

    public Response postForEntity(String url, Request request) {
        HttpEntity<Request> requestEntity = buildRequestEntity(request);

        try {
            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(url, requestEntity, getResponseType());

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RestTemplateException(getExceptionMessage(request));
            }

            return responseEntity.getBody();
        } catch (RestClientException e) {
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
