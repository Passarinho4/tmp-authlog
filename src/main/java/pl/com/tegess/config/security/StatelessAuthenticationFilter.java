package pl.com.tegess.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class StatelessAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        Authentication authentication = authenticationService.getAuthentication(httpRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
