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
        @DisplayName("should return null for missing / empty header")
        void shouldReturnNull_forMissingOrEmptyHeader() {
            String headerName = "foobar";

            MockHttpServletRequest request = new MockHttpServletRequest();

            assertNull(new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request));

            request.addHeader(headerName, "");

            assertNull(new ApiKeyRequestFilter(headerName).getPreAuthenticatedPrincipal(request));

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

            assertTrue(principal instanceof ApiKeyPrincipal);

            assertEquals(apiKey, ((ApiKeyPrincipal)principal).apiKey());
        }
    }

    @Test
    void getPreAuthenticatedCredentials() {
        assertEquals("N/A", new ApiKeyRequestFilter("foo").getPreAuthenticatedCredentials(new MockHttpServletRequest()));
        assertEquals("N/A", new ApiKeyRequestFilter("bar").getPreAuthenticatedCredentials(new MockHttpServletRequest()));
    }
}
