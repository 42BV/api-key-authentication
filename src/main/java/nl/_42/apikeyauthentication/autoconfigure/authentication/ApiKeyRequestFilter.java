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
        return new ApiKeyPrincipal(request.getHeader(headerName));
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A"; // The credentials are not needed when using an API key as PreAuthenticated token.
    }

}
