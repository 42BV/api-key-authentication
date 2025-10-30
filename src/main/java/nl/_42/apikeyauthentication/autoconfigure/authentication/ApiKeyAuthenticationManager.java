package nl._42.apikeyauthentication.autoconfigure.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthenticationManager implements AuthenticationManager {

    private final Collection<String> authorizedApiKeys;

    public ApiKeyAuthenticationManager(Collection<String> authorizedApiKeys) {
        this.authorizedApiKeys = authorizedApiKeys;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof ApiKeyPrincipal)) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }

        String key = ((ApiKeyPrincipal) authentication.getPrincipal()).apiKey();
        if (!authorizedApiKeys.contains(key != null ? key : "")) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }

        authentication.setAuthenticated(true);
        return authentication;
    }
}
