package nl._42.apikeyauthentication.autoconfigure.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import nl._42.apikeyauthentication.autoconfigure.authentication.ApiKeyPrincipal;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class FooFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(HttpStatus.I_AM_A_TEAPOT.value());

        String apiKey = "<<not set>>";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof PreAuthenticatedAuthenticationToken) {
            apiKey = ((ApiKeyPrincipal)auth.getPrincipal()).getApiKey();
        }
        httpServletResponse.getOutputStream().write(("I AM A TEAPOT AND AM USING KEY " + apiKey + " :D").getBytes(StandardCharsets.UTF_8));
    }
}
