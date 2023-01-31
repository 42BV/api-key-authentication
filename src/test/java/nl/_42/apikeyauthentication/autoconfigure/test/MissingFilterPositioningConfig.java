package nl._42.apikeyauthentication.autoconfigure.test;

import java.util.Set;

import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfiguration;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurationBuilder;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@Profile("api-key-filter-positioning-missing")
@EnableWebSecurity
public class MissingFilterPositioningConfig {

    public static final String ALLOWED_KEY = "filter-position-missing-1234567890";

    private static IllegalArgumentException exception;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        exception = null;
        http = http.addFilterBefore(new FooFilter(), BasicAuthenticationFilter.class);

        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .authorizedApiKeys(Set.of(ALLOWED_KEY))
                .addFilterBeforeClass(null)
                .addFilterAfterClass(null)
                .build();

        try {
            ApiKeyAuthenticationConfigurer.configure(config, http);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        return http.build();
    }

    public static IllegalArgumentException getException() {
        return exception;
    }


}
