package nl._42.apikeyauthentication.autoconfigure.authentication;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class ApiKeyRequestFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String headerName;

    public ApiKeyRequestFilter(String headerName) {
        if (headerName == null || headerName.equals("")) {
            throw new IllegalArgumentException("headerName cannot be blank!");
        }
        this.headerName = headerName;
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String apiKeyValue = request.getHeader(headerName);
        if (apiKeyValue == null || apiKeyValue.equals("")) {
            return null;
        }
        return new ApiKeyPrincipal(apiKeyValue);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A"; // The credentials are not needed when using an API key as PreAuthenticated token.
    }

}
