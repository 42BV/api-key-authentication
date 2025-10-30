package nl._42.apikeyauthentication.autoconfigure.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ApiKeyRequestFilterTest {

    @Test
    void throwsForEmptyHeader() {
        assertThrows(IllegalArgumentException.class, () -> new ApiKeyRequestFilter(null));
        assertThrows(IllegalArgumentException.class, () -> new ApiKeyRequestFilter(""));
        assertDoesNotThrow(() -> new ApiKeyRequestFilter("test"));
    }

    @Nested
    class getPreAuthenticatedPrincipal {

        @Test
        @DisplayName("should return api key principal without key for missing / empty header")
        void shouldReturnPrincipalWithoutKey_forMissingOrEmptyHeader() {
            String headerName = "foobar";

            MockHttpServletRequest request = new MockHttpServletRequest();

            Object principal = new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request);
            assertInstanceOf(ApiKeyPrincipal.class, principal);
            assertNull(((ApiKeyPrincipal)principal).apiKey());

            request.addHeader(headerName, "");

            principal = new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request);
            assertInstanceOf(ApiKeyPrincipal.class, principal);
            assertEquals("", ((ApiKeyPrincipal)principal).apiKey());

            request = new MockHttpServletRequest();
            request.addHeader(headerName, "test");
            assertNotNull(new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request));
        }

        @Test
        @DisplayName("should return api key principal for valid header")
        void shouldReturnPrincipal_forValidHeader() {
            String headerName = "foobar";
            String apiKey = "test";

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(headerName, apiKey);
            Object principal = new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request);

            assertInstanceOf(ApiKeyPrincipal.class, principal);

            assertEquals(apiKey, ((ApiKeyPrincipal)principal).apiKey());
        }
    }

    @Test
    void getPreAuthenticatedCredentials() {
        assertEquals("N/A", new ApiKeyRequestFilter("foo").getPreAuthenticatedCredentials(new MockHttpServletRequest()));
        assertEquals("N/A", new ApiKeyRequestFilter("bar").getPreAuthenticatedCredentials(new MockHttpServletRequest()));
    }
}
