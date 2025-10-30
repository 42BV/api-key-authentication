[![Build Status](https://github.com/42BV/api-key-authentication/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/42BV/api-key-authentication/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/adb672105a62466085415f7ff0866357)](https://www.codacy.com/gh/42BV/api-key-authentication/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=42BV/api-key-authentication&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/42BV/api-key-authentication?branch=main)](https://bettercodehub.com/)
[![codecov](https://codecov.io/gh/42BV/api-key-authentication/branch/main/graph/badge.svg)](https://codecov.io/gh/42BV/api-key-authentication)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.42/api-key-authentication/badge.svg)](https://maven-badges.herokuapp.com/maven-central/nl.42/api-key-authentication)
[![Javadoc](https://www.javadoc.io/badge/nl.42/api-key-authentication.svg)](https://www.javadoc.io/doc/nl.42/api-key-authentication)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# 42 API Key Authentication

A library to easily configure API Key authentication in (parts of) your Spring Boot Application.

## Features

- Easily configure API Key authentication for (a portion) of endpoints in your app
- Support for specifying multiple keys from multiple sources
- Configurable header name
- Configurable filter placement in the FilterChain

## Requirements
- Java 17 and Spring Boot 3.0.0+ (use version 1.0.0 of this library if still using Spring Boot 2.x)
- Spring Security
- Spring Web

## Setting up API Key authentication

- You must have the following components in your application:
   * A list of authorized API keys (these can come from your `application.yml`, for example)
   * One or more endpoints to protect
   
- The maven dependencies you need:

```xml
<dependencies>
    <dependency>
        <groupId>nl.42</groupId>
        <artifactId>api-key-authentication</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>`
</dependencies>
```

- Create a class annotated by `@Configuration` which defines a `SecurityFilterChain` bean. Add it to your app:
 
```java
@Configuration
@EnableWebSecurity
public class ApiKeyConfig {


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // You can easily configure this library using the Builder...
    // ... or you can create your very own implementation of ApiKeyAuthenticationConfiguration
    ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder() 
            .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2)) // The API Keys that will be granted access to the endpoints
            .antPattern("/public-api/**") // The endpoints you want to protect by API Key (basic pattern). Defaults to 'all endpoints'.
            .requestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/public-api/v1/hello"), new AntPathRequestMatcher("/public-api/v1/goodbye"))) // The endpoints you want to protect by API Key (advanced matching)
            .addFilterBeforeClass(BasicAuthenticationFilter.class) // Customize where the API Key check will be inserted (defaults to before BasicAuthenticationFilter)
            .addFilterAfterClass(FooFilter.class) // Customize where the API Key check will be inserted  (defaults to null)
            .headerName("my-awesome-api-key") // Customize the header name (defaults to x-api-key)
            .build();

    ApiKeyAuthenticationConfigurer.configure(config, http);
    
    return http.build();
  }
}
```

## Customization

### Using a custom header name
The default header name will be `x-api-key`, but you can override it as following:
 
 ```java
@Configuration
@EnableWebSecurity
public class ApiKeyConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
            .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
            .headerName("my-awesome-api-key-header-name")
            .build();

    ApiKeyAuthenticationConfigurer.configure(config, http);
    
    return http.build();
  }
}
 ```

### Using a custom response code for failed authentications
By default, your security settings will be configured to return HTTP 401 'Unauthorized' for requests that fail
to authenticate using an API Key.

You can customize this to return a different HTTP status code or error handler.
 
 ```java
@Configuration
@EnableWebSecurity
public class ApiKeyConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
            .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
            // Option 1: custom authenticationFailureEntryPoint which sets Http 402 'Payment Required' as status.
            .authenticationFailureEntryPoint(new HttpStatusEntryPoint(HttpStatus.PAYMENT_REQUIRED))
            // Option 2: If you want to keep the default settings of Spring (HTTP 403 'Forbidden'), explicitly set this to null:
            .authenticationFailureEntryPoint(null)
            .build();

    ApiKeyAuthenticationConfigurer.configure(config, http);
    
    return http.build();
  }
}
 ```

### Advanced request matching
By default, all endpoints will be secured. You can either use a String-based ANT Pattern or a `RequestMatcher` to customize which endpoints to protect.
 
 ```java
@Configuration
@EnableWebSecurity
public class ApiKeyConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
            .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
            .requestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/public-api/v1/**"), new AntPathRequestMatcher("/public-api/v2/**")))
            .build();

    ApiKeyAuthenticationConfigurer.configure(config, http);
    
    return http.build();
  }
}
 ```
**NOTE:** Unless configured otherwise, endpoints not matched by the request matcher will **NOT** be secured!

### Customizing the timing of the check
The check will be done by a `Filter` in the `FilterChain` of each request.

If you want to change when this check happens (e.g. perform other checks first or afterwards), 
either use the `addFilterBeforeClass` and `addFilterAfterClass` methods of the Builder:
 
 ```java
@Configuration
@EnableWebSecurity
public class ApiKeyConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthenticationConfiguration config = ApiKeyAuthenticationConfigurationBuilder.builder()
            .authorizedApiKeys(Set.of(ALLOWED_KEY_1, ALLOWED_KEY_2))
            .addFilterBeforeClass(BasicAuthenticationFilter.class)
            .addFilterAfterClass(FooFilter.class)
            .build();

    ApiKeyAuthenticationConfigurer.configure(config, http);
    
    return http.build();
  }
}
 ```

**NOTE:** You can only specify one position. The `addFilterAfterClass` has a higher priority than `addFilterBeforeClass`.
