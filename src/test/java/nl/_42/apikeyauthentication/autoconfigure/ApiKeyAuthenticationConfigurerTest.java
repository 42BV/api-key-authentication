package nl._42.apikeyauthentication.autoconfigure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl._42.apikeyauthentication.autoconfigure.test.ApiKeyCustomHeaderNameConfig;
import nl._42.apikeyauthentication.autoconfigure.test.ApiKeyOnAllEndpointsConfig;
import nl._42.apikeyauthentication.autoconfigure.test.ApiKeyOnAntPatternConfig;
import nl._42.apikeyauthentication.autoconfigure.test.ApiKeyOnCustomRequestMatcherConfig;
import nl._42.apikeyauthentication.autoconfigure.test.FilterPositioningAfterConfig;
import nl._42.apikeyauthentication.autoconfigure.test.FilterPositioningBeforeConfig;
import nl._42.apikeyauthentication.autoconfigure.test.MissingFilterPositioningConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ApiKeyAuthenticationConfigurerTest {

    @Nested
    class allDefaultConfig extends ApiKeyScenarioTest.ApiKeyOnAllEndpointsTest {

        @Test
        @DisplayName("should enable API Key authentication on ALL application endpoints if no ant pattern or requestMatcher is configured")
        void shouldEnableOnAllEndpoints() {
            RestTemplate allowedRestTemplate1 = buildRestTemplate(ApiKeyOnAllEndpointsConfig.ALLOWED_KEY_1);
            RestTemplate allowedRestTemplate2 = buildRestTemplate(ApiKeyOnAllEndpointsConfig.ALLOWED_KEY_2);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Both configured keys should be allowed
            HttpEntity<Map<String, Object>> publicApiResult1 = allowedRestTemplate1.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult2 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));

            // Foo key should be forbidden
            HttpClientErrorException errorExceptionForbidden1 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionForbidden2 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/private-api/users", HttpMethod.GET, null, List.class));
            HttpClientErrorException errorExceptionForbidden3 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/foo", HttpMethod.GET, null, String.class));

            assertEquals("hi", Objects.requireNonNull(publicApiResult1.getBody()).get("message"));
            assertEquals("hi", Objects.requireNonNull(publicApiResult2.getBody()).get("message"));
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden1.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden2.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden3.getStatusCode());
        }
    }

    @Nested
    class customHeaderNameConfig extends ApiKeyScenarioTest.ApiKeyCustomHeaderNameTest {

        @Test
        @DisplayName("should enable API Key authentication on ALL application endpoints, but with a custom header name")
        void shouldEnableOnAllEndpoints() {
            RestTemplate allowedRestTemplate1 = buildRestTemplate(ApiKeyCustomHeaderNameConfig.ALLOWED_KEY_1);
            RestTemplate allowedRestTemplate2 = buildRestTemplate(ApiKeyCustomHeaderNameConfig.ALLOWED_KEY_2);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Both configured keys should be allowed if using the correct header name
            HttpHeaders key1Headers = new HttpHeaders();
            key1Headers.add(ApiKeyCustomHeaderNameConfig.HEADER_NAME, ApiKeyCustomHeaderNameConfig.ALLOWED_KEY_1);
            HttpHeaders key2Headers = new HttpHeaders();
            key2Headers.add(ApiKeyCustomHeaderNameConfig.HEADER_NAME, ApiKeyCustomHeaderNameConfig.ALLOWED_KEY_2);
            HttpHeaders keyFooHeaders = new HttpHeaders();
            keyFooHeaders.add(ApiKeyCustomHeaderNameConfig.HEADER_NAME, "foo");

            HttpEntity<Void> httpEntityKey1 = new HttpEntity<>(null, key1Headers);
            HttpEntity<Void> httpEntityKey2 = new HttpEntity<>(null, key2Headers);
            HttpEntity<Void> httpEntityKeyFoo = new HttpEntity<>(null, keyFooHeaders);

            HttpEntity<Map<String, Object>> publicApiResult1 = allowedRestTemplate1.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, httpEntityKey1, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult2 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, httpEntityKey2, ParameterizedTypeReference.forType(Map.class));

            // Foo key should be forbidden even if using the correct header name.
            HttpClientErrorException errorExceptionForbidden1 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, httpEntityKeyFoo, Map.class));

            // All keys should be forbidden if not using the correct header name (the default is x-api-key, by specifying a null httpEntity this will be used).
            HttpClientErrorException errorExceptionForbidden2 = assertThrows(HttpClientErrorException.class, () -> allowedRestTemplate1.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionForbidden3 = assertThrows(HttpClientErrorException.class, () -> allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionForbidden4 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));

            assertEquals("hi", Objects.requireNonNull(publicApiResult1.getBody()).get("message"));
            assertEquals("hi", Objects.requireNonNull(publicApiResult2.getBody()).get("message"));
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden1.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden2.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden3.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden4.getStatusCode());
        }
    }

    @Nested
    class antPatternConfig extends ApiKeyScenarioTest.ApiKeyAntPatternTest {

        @Test
        @DisplayName("should enable API Key authentication only on endpoints matching the given string antPattern")
        public void shouldConfigureSecurity_withAntPattern() {
            RestTemplate allowedRestTemplate1 = buildRestTemplate(ApiKeyOnAntPatternConfig.ALLOWED_KEY_1);
            RestTemplate allowedRestTemplate2 = buildRestTemplate(ApiKeyOnAntPatternConfig.ALLOWED_KEY_2);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Both configured keys should be allowed
            HttpEntity<Map<String, Object>> publicApiResult1 = allowedRestTemplate1.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult2 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult3 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/goodbye", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult4 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/sleep-well", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));

            // Foo key should be forbidden on endpoints matching the ant pattern (/public-api/**)
            HttpClientErrorException errorExceptionForbidden = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));

            // Foo key should NOT be forbidden on other endpoints than /public-api, since no security config exists for those.
            HttpEntity<List<Map<String, Object>>> privateApiResult = forbiddenTemplate.exchange(baseUrl + "/private-api/users", HttpMethod.GET, null, ParameterizedTypeReference.forType(List.class));
            HttpClientErrorException errorExceptionNotFound = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/foo", HttpMethod.GET, null, String.class));

            assertEquals("hi", Objects.requireNonNull(publicApiResult1.getBody()).get("message"));
            assertEquals("hi", Objects.requireNonNull(publicApiResult2.getBody()).get("message"));
            assertEquals("goodbye", Objects.requireNonNull(publicApiResult3.getBody()).get("message"));
            assertEquals("sleep well", Objects.requireNonNull(publicApiResult4.getBody()).get("message"));
            assertEquals("John", Objects.requireNonNull(privateApiResult.getBody()).get(0).get("firstName"));
            assertEquals("Astley", Objects.requireNonNull(privateApiResult.getBody()).get(1).get("lastName"));
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden.getStatusCode());
            assertEquals(HttpStatus.NOT_FOUND, errorExceptionNotFound.getStatusCode());
        }
    }

    @Nested
    class customRequestMatcherConfig extends ApiKeyScenarioTest.ApiKeyCustomRequestMatcherTest {

        @Test
        @DisplayName("should enable API Key authentication only on endpoints matching the given requestMatcher (has precedence over antPattern")
        public void shouldConfigureSecurity_withAntPattern() {
            RestTemplate allowedRestTemplate1 = buildRestTemplate(ApiKeyOnCustomRequestMatcherConfig.ALLOWED_KEY_1);
            RestTemplate allowedRestTemplate2 = buildRestTemplate(ApiKeyOnCustomRequestMatcherConfig.ALLOWED_KEY_2);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Both configured keys should be allowed
            HttpEntity<Map<String, Object>> publicApiResult1 = allowedRestTemplate1.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult2 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult3 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/goodbye", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));
            HttpEntity<Map<String, Object>> publicApiResult4 = allowedRestTemplate2.exchange(baseUrl + "/public-api/v1/sleep-well", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));

            // Foo key should be forbidden on endpoints matching the requestMatcher (/public-api/hello, /public-api/goodbye)
            HttpClientErrorException errorExceptionForbidden1 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionForbidden2 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/goodbye", HttpMethod.GET, null, Map.class));

            // Foo key should NOT be forbidden on /public-api/sleep-well, since it is not matched by the requestMatcher.
            HttpEntity<Map<String, Object>> publicApiResult5 = forbiddenTemplate.exchange(baseUrl + "/public-api/v1/sleep-well", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class));

            assertEquals("hi", Objects.requireNonNull(publicApiResult1.getBody()).get("message"));
            assertEquals("hi", Objects.requireNonNull(publicApiResult2.getBody()).get("message"));
            assertEquals("goodbye", Objects.requireNonNull(publicApiResult3.getBody()).get("message"));
            assertEquals("sleep well", Objects.requireNonNull(publicApiResult4.getBody()).get("message"));
            assertEquals("sleep well", Objects.requireNonNull(publicApiResult5.getBody()).get("message"));
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden1.getStatusCode());
            assertEquals(HttpStatus.FORBIDDEN, errorExceptionForbidden2.getStatusCode());
        }
    }

    @Nested
    class insertFilterBeforeConfig extends ApiKeyScenarioTest.ApiKeyFilterPositioningBeforeTest {

        @Test
        @DisplayName("should enable API Key authentication on all endpoints, since the filter is inserted before a havoc breaking filter")
        public void shouldConfigureSecurity_withFilterPositioning() {
            RestTemplate allowedRestTemplate = buildRestTemplate(FilterPositioningBeforeConfig.ALLOWED_KEY);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Configured key should be allowed, but returns "I am teapot" due to havoc breaking filter.
            HttpClientErrorException errorExceptionTeapot1 = assertThrows(HttpClientErrorException.class, () -> allowedRestTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class)));

            // Foo key should reach the teapot filter as well, but since it is forbidden no key will be logged.
            HttpClientErrorException errorExceptionForbidden1 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionForbidden2 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/private-api/users", HttpMethod.GET, null, List.class));
            HttpClientErrorException errorExceptionForbidden3 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/foo", HttpMethod.GET, null, String.class));

            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionTeapot1.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY filter-position-before-1234567890 :D", errorExceptionTeapot1.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionForbidden1.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionForbidden1.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionForbidden2.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionForbidden2.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionForbidden3.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionForbidden3.getResponseBodyAsString());
        }
    }

    @Nested
    class insertFilterAfterConfig extends ApiKeyScenarioTest.ApiKeyFilterPositioningAfterTest {

        @Test
        @DisplayName("should always return bogus response, since our filter is positioned after the havoc-wreaking filter")
        public void shouldConfigureSecurity_withFilterPositioning() {
            RestTemplate allowedRestTemplate = buildRestTemplate(FilterPositioningAfterConfig.ALLOWED_KEY);
            RestTemplate forbiddenTemplate = buildRestTemplate("foo");

            // Configured key should return "I am teapot" without setting authentication, due to havoc breaking filter being before api key filter.
            HttpClientErrorException errorExceptionTeapot1 = assertThrows(HttpClientErrorException.class, () -> allowedRestTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, ParameterizedTypeReference.forType(Map.class)));

            // Foo key should also return teapot error, since the teapot error is generated before the API Key filter is reached (and execution stops there).
            HttpClientErrorException errorExceptionTeapot2 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/public-api/v1/hello", HttpMethod.GET, null, Map.class));
            HttpClientErrorException errorExceptionTeapot3 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/private-api/users", HttpMethod.GET, null, List.class));
            HttpClientErrorException errorExceptionTeapot4 = assertThrows(HttpClientErrorException.class, () -> forbiddenTemplate.exchange(baseUrl + "/foo", HttpMethod.GET, null, String.class));

            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionTeapot1.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionTeapot1.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionTeapot2.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionTeapot2.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionTeapot3.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionTeapot3.getResponseBodyAsString());
            assertEquals(HttpStatus.I_AM_A_TEAPOT, errorExceptionTeapot4.getStatusCode());
            assertEquals("I AM A TEAPOT AND AM USING KEY <<not set>> :D", errorExceptionTeapot4.getResponseBodyAsString());
        }
    }

    @Nested
    class insertFilterPositionMissingConfig extends ApiKeyScenarioTest.ApiKeyFilterPositioningMissingTest {

        @Test
        @DisplayName("should throw IllegalArgumentException during app configuration if the filter positioning is missing")
        public void shouldThrowForMissingPositioning() {
            IllegalArgumentException e = MissingFilterPositioningConfig.getException();
            assertNotNull(e);

            assertEquals("Either AddFilterBeforeClass or AddFilterAfterClass must be specified.", e.getMessage());
        }
    }
}
