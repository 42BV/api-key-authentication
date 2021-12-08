
package nl._42.apikeyauthentication.autoconfigure;

import java.util.Collection;
import java.util.Objects;

import javax.servlet.Filter;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Builder
@Slf4j
public class ApiKeyAuthenticationConfigurationBuilder implements ApiKeyAuthenticationConfiguration {

    public static final String DEFAULT_HEADER_NAME = "x-api-key";
    public static final String DEFAULT_REQUEST_PATTERN = "**";
    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_REQUEST_PATTERN);

    @Builder.Default
    private final String headerName = DEFAULT_HEADER_NAME;

    @Builder.Default
    private final String antPattern = DEFAULT_REQUEST_PATTERN;

    @Builder.Default
    private final RequestMatcher requestMatcher = DEFAULT_REQUEST_MATCHER;

    private final Collection<String> authorizedApiKeys;

    @Builder.Default
    private final Class<? extends Filter> addFilterBeforeClass = BasicAuthenticationFilter.class;

    private final Class<? extends Filter> addFilterAfterClass;

    /**
     * The name of the header to read the API key from.
     * Defaults to 'x-api-key'.
     * @return Name of the HTTP header which contains the API Key.
     */
    @Override
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Request matcher to get the endpoints to enable API Key authentication for.
     * Defaults to a request matcher that matches any request.
     * @return Returns a {@link RequestMatcher} that determines which endpoints are to be secured with an API Key.
     */
    @Override
    public RequestMatcher getRequestMatcher() {
        if (!Objects.equals(requestMatcher, DEFAULT_REQUEST_MATCHER)) {
            //noinspection ConstantConditions IDE cannot detect that Lombok Builder ignores final field 'antPattern'. This log statement can actually be reached.
            if (!Objects.equals(antPattern, DEFAULT_REQUEST_PATTERN)) {
                log.warn("Ignoring custom provided antPattern, since a requestMatcher has already been supplied. If you want ot use a String-based antPattern, remove the custom requestMatcher.");
            }
            return requestMatcher;
        }

        return new AntPathRequestMatcher(antPattern);
    }

    /**
     * A collection of API Keys that are authorized to access the endpoints matching the AntPattern.
     * @return Returns a collection of keys which grant access to the secured endpoints
     */
    @Override
    public Collection<String> getAuthorizedApiKeys() {
        return authorizedApiKeys;
    }

    /**
     * Class in the FilterChain to insert the filter before
     * Defaults to {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter}
     * @return Returns a class to insert the filter before in the filterChain.
     */
    @Override
    public Class<? extends Filter> getAddFilterBeforeClass() {
        return addFilterBeforeClass;
    }

    /**
     * Class in the FilterChain to insert the filter before
     * Defaults to null
     * @return Returns a class to insert the filter before in the filterChain.
     */
    @Override
    public Class<? extends Filter> getAddFilterAfterClass() {
        return addFilterAfterClass;
    }
}
