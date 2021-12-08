package nl._42.apikeyauthentication.autoconfigure;

import nl._42.apikeyauthentication.autoconfigure.authentication.ApiKeyAuthenticationManager;
import nl._42.apikeyauthentication.autoconfigure.authentication.ApiKeyRequestFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

public class ApiKeyAuthenticationConfigurer {

    private ApiKeyAuthenticationConfigurer() {}

    /**
     * Configures API Key authentication using the given ApiKeyAuthenticationConfiguration and HttpSecurity
     * @param configuration Settings to apply for the authentication
     * @param http Spring Security HttpSecurity builder
     * @throws Exception If SessionManagement or CSRF cannot be configured
     */
    public static void configure(ApiKeyAuthenticationConfiguration configuration, HttpSecurity http) throws Exception {
        /*
         * We don't need to create sessions, all the requests must provide
         * a valid API Key, so we recheck each time
         */
        SessionManagementConfigurer<HttpSecurity> configurer = http.requestMatcher(configuration.getRequestMatcher())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
         * An API key is used to authenticate the incoming requests. For this, an instance of the ApiKeyRequestFilter is needed.
         */
        ApiKeyRequestFilter filter = new ApiKeyRequestFilter(configuration.getHeaderName());
        filter.setAuthenticationManager(new ApiKeyAuthenticationManager(configuration.getAuthorizedApiKeys()));

        HttpSecurity httpWithFilter;

        if (configuration.getAddFilterAfterClass() != null) {
            httpWithFilter = configurer.and()
                    .addFilterAfter(filter, configuration.getAddFilterAfterClass());
        } else if (configuration.getAddFilterBeforeClass() != null) {
            httpWithFilter = configurer.and()
                    .addFilterBefore(filter, configuration.getAddFilterBeforeClass());
        } else {
            throw new IllegalArgumentException("Either AddFilterBeforeClass or AddFilterAfterClass must be specified.");
        }

        /*
         * CSRF interferes with POST / PUT / DELETE requests securecd by the API Key. Therefore, it must be disabled.
         */
        httpWithFilter
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated();
    }

}
