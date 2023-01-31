package nl._42.apikeyauthentication.autoconfigure;

import java.util.Collections;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractSpringTest {

    protected String baseUrl;

    @BeforeEach
    public void setup(@LocalServerPort int port) {
        baseUrl = "http://localhost:" + port + "/";
    }

    protected RestTemplate buildRestTemplate(String apiKey) {
        return buildRestTemplate(apiKey, new HttpHeaders());
    }

    protected RestTemplate buildRestTemplate(String apiKey, HttpHeaders headers) {

        try {
            CloseableHttpClient client = HttpClients.custom()
                    .setDefaultHeaders(StringUtils.isNotBlank(apiKey) ? Collections.singleton(new BasicHeader(ApiKeyAuthenticationConfigurationBuilder.DEFAULT_HEADER_NAME, apiKey)) : Collections.emptyList())
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
            BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);

            return new RestTemplateBuilder()
                    .requestFactory(() -> bufferingClientHttpRequestFactory)
                    .additionalInterceptors((request, body, execution) -> {
                        HttpHeaders httpHeaders = request.getHeaders();
                        if (apiKey != null && !apiKey.equals("") && !httpHeaders.containsKey(ApiKeyAuthenticationConfigurationBuilder.DEFAULT_HEADER_NAME)) {
                            httpHeaders.set(ApiKeyAuthenticationConfigurationBuilder.DEFAULT_HEADER_NAME, apiKey);
                        }
                        httpHeaders.addAll(headers);
                        return execution.execute(request, body);
                    })
                    .build();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
