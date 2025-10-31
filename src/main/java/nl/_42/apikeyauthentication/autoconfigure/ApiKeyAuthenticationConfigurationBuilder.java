
package nl._42.apikeyauthentication.autoconfigure;

import java.util.Collection;
import java.util.Objects;

import jakarta.servlet.Filter;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Builder to configure an instance of ApiKeyAuthenticationConfiguration.
 */
@Builder
@Slf4j
public class ApiKeyAuthenticationConfigurationBuilder implements ApiKeyAuthenticationConfiguration {

    /**
     * The default HTTP header name to use: x-api-key
     */
    public static final String DEFAULT_HEADER_NAME = "x-api-key";

    /**
     * The default request pattern (Spring PathPattern) to apply Api Key security on.
     */
    public static final String DEFAULT_REQUEST_PATTERN = "/**";
    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults().matcher(DEFAULT_REQUEST_PATTERN);

    @Builder.Default
    private final String headerName = DEFAULT_HEADER_NAME;

    @Builder.Default
    private final String pathPattern = DEFAULT_REQUEST_PATTERN;

    @Builder.Default
    private final RequestMatcher requestMatcher = DEFAULT_REQUEST_MATCHER;

    private final Collection<String> authorizedApiKeys;

    @Builder.Default
    private final AuthenticationEntryPoint authenticationFailureEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

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
            //noinspection ConstantConditions IDE cannot detect that Lombok Builder ignores final field 'pathPattern'. This log statement can actually be reached.
            if (!Objects.equals(pathPattern, DEFAULT_REQUEST_PATTERN)) {
                log.warn("Ignoring custom provided pathPattern, since a requestMatcher has already been supplied. If you want ot use a String-based pathPattern, remove the custom requestMatcher.");
            }
            return requestMatcher;
        }

        return PathPatternRequestMatcher.withDefaults().matcher(pathPattern);
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

    /**
     * Specify the {@link AuthenticationEntryPoint} which handles failed authentications.
     * By default, we return HTTP 401 'Unauthorized' via {@link HttpStatusEntryPoint}.
     * This best matches the situation for incorrect API Keys, which are unauthorized to use the application.
     * You can override this method to specify your own {@link AuthenticationEntryPoint}, such as for another
     * HTTP status code or for custom error handling.
     * You can also explicitly set this to 'null' to use the default authentication failure handler of Spring.
     * @return Returns the {@link AuthenticationEntryPoint} to use for failed authentications.
     */
    @Override
    public AuthenticationEntryPoint getAuthenticationFailureEntryPoint() {
        return authenticationFailureEntryPoint;
    }
}
