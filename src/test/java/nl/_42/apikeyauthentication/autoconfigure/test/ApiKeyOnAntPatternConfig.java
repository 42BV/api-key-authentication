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

@Configuration
@Profile("api-key-ant-pattern")
@EnableWebSecurity
public class ApiKeyOnAntPatternConfig {

    public static final String ALLOWED_KEY_1 = "public-1234567890";
    public static final String ALLOWED_KEY_2 = "public-abcdefghij";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .antPattern("/public-api/**")
                .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
                .build();

        ApiKeyAuthenticationConfigurer.configure(config, http);

        return http.build();
    }
}
