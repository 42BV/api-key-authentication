package nl._42.apikeyauthentication.autoconfigure.test;

import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfiguration;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurationBuilder;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
@Profile("null-authentication-entry-point")
@EnableWebSecurity
public class NullAuthenticationEntryPointConfig {

    public static final String ALLOWED_KEY = "null-aep-1234567890";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .authorizedApiKeys(Set.of(ALLOWED_KEY))
                // by deliberately assigning authenticationFailureEntryPoint to null, we use the defaults from Spring.
                .authenticationFailureEntryPoint(null)
                .build();

        ApiKeyAuthenticationConfigurer.configure(config, http);

        return http.build();
    }
}
