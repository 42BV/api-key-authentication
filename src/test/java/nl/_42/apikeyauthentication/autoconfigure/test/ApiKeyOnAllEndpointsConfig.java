package nl._42.apikeyauthentication.autoconfigure.test;

import java.util.Set;

import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfiguration;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurationBuilder;
import nl._42.apikeyauthentication.autoconfigure.ApiKeyAuthenticationConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Profile("api-key-all-endpoints")
@EnableWebSecurity
public class ApiKeyOnAllEndpointsConfig extends WebSecurityConfigurerAdapter {

    public static final String ALLOWED_KEY_1 = "all-1234567890";
    public static final String ALLOWED_KEY_2 = "all-abcdefghij";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
                .build();

        ApiKeyAuthenticationConfigurer.configure(config, http);
    }
}
