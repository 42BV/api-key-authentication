package nl._42.apikeyauthentication.autoconfigure.authentication;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Spring HTTP request filter that reads the API Key from the HTTP Headers.
 */
public class ApiKeyRequestFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String headerName;

    /**
     * Construct the ApiKeyRequestFilter with the HTTP header name to check.
     * @param headerName Name of the HTTP header that holds the API Key.
     */
    public ApiKeyRequestFilter(String headerName) {
        if (headerName == null || headerName.isEmpty()) {
            throw new IllegalArgumentException("headerName cannot be blank!");
        }
        this.headerName = headerName;
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return new ApiKeyPrincipal(request.getHeader(headerName));
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A"; // The credentials are not needed when using an API key as PreAuthenticated token.
    }

}
