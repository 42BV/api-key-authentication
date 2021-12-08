package nl._42.apikeyauthentication.autoconfigure.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApiKeyPrincipalTest {

    @Test
    void getApiKey() {
        assertEquals("test", new ApiKeyPrincipal("test").getApiKey());
    }
}
