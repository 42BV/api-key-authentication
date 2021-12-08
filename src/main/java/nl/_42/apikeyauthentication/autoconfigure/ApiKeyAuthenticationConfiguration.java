package nl._42.apikeyauthentication.autoconfigure;

import java.util.Collection;

import javax.servlet.Filter;

import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * The configuration specifies various things, such as which header to read, which endpoints to secure and which keys are granted access.
 */
public interface ApiKeyAuthenticationConfiguration {

    /**
     * The name of the header to read the API key from.
     * @return Name of the HTTP header which contains the API Key.
     */
    String getHeaderName();

    /**
     * Request matcher to get the endpoints to enable API Key authentication for.
     * @return Returns a {@link RequestMatcher} that determines which endpoints are to be secured with an API Key.
     */
    RequestMatcher getRequestMatcher();

    /**
     * A collection of API Keys that are authorized to access the endpoints matching the AntPattern.
     * @return Returns a collection of keys which grant access to the secured endpoints
     */
    Collection<String> getAuthorizedApiKeys();

    /**
     * Class in the FilterChain to insert the filter before
     * @return Returns a class to insert the filter before in the filterChain.
     */
    Class<? extends Filter> getAddFilterBeforeClass();

    /**
     * Class in the FilterChain to insert the filter after.
     * @return Returns a class to insert the filter before in the filterChain.
     */
    Class<? extends Filter> getAddFilterAfterClass();
}
