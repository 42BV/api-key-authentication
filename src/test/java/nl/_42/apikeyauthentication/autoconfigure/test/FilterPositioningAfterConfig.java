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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@Profile("api-key-filter-positioning-after")
@EnableWebSecurity
public class FilterPositioningAfterConfig extends WebSecurityConfigurerAdapter {

    public static final String ALLOWED_KEY = "filter-position-after-1234567890";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.addFilterBefore(new FooFilter(), BasicAuthenticationFilter.class);

        ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
                .authorizedApiKeys(Set.of(ALLOWED_KEY))
                .addFilterAfterClass(FooFilter.class)
                .build();
        ApiKeyAuthenticationConfigurer.configure(config, http);
    }


}
