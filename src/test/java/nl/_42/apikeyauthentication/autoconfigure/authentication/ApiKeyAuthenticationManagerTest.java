package nl._42.apikeyauthentication.autoconfigure.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class ApiKeyAuthenticationManagerTest {

    @Nested
    class authenticate {

        @Test
        @DisplayName("should authenticate if principal instance of apiKeyPrincipal and key is authorized")
        void shouldAuthenticateValidKey() {
            ApiKeyAuthenticationManager authenticationManager = new ApiKeyAuthenticationManager(List.of("test1234"));

            Authentication authentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(new ApiKeyPrincipal("test1234"), "N/A"));

            assertTrue(authentication.isAuthenticated());
        }

        @Test
        @DisplayName("should throw BadCredentialsException if the key is not valid")
        void shouldThrowForInvalidKey() {
            ApiKeyAuthenticationManager authenticationManager = new ApiKeyAuthenticationManager(List.of("test2345"));

            BadCredentialsException e = assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(new ApiKeyPrincipal("test1234"), "N/A")));

            assertEquals("The API key was not found or not the expected value.", e.getMessage());
        }

        @Test
        @DisplayName("should throw BadCredentialsException if the principal is not an instance of ApiKeyPrincipal")
        void shouldThrowForInvalidPrincipal() {
            ApiKeyAuthenticationManager authenticationManager = new ApiKeyAuthenticationManager(List.of("test2345"));

            BadCredentialsException e = assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken("foo", "N/A")));

            assertEquals("The API key was not found or not the expected value.", e.getMessage());
        }
    }

}
