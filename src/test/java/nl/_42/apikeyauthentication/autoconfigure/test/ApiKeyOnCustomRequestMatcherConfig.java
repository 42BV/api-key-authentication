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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@Profile("api-key-custom-request-matcher")
@EnableWebSecurity
public class ApiKeyOnCustomRequestMatcherConfig {

    public static final String ALLOWED_KEY_1 = "custom-request-matcher-1234567890";
    public static final String ALLOWED_KEY_2 = "custom-request-matcher-abcdefghij";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .antPattern("/private-api/**") // Will be ignored, the requestMatcher takes precedence over a String antPattern.
                .requestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/public-api/v1/hello"), new AntPathRequestMatcher("/public-api/v1/goodbye")))
                .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
                .build();

        ApiKeyAuthenticationConfigurer.configure(config, http);

        return http.build();
    }
}
