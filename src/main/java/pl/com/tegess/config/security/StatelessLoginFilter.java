package pl.com.tegess.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import pl.com.tegess.domain.admin.Admin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private AdminService adminService;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public StatelessLoginFilter(String urlMapping, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String header = Optional.ofNullable(request.getHeader("Authorization"))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Header Authorization not found"));
        try {
            String login = header.split(":")[0];
            String password = header.split(":")[1].split(",")[0];

            UsernamePasswordAuthenticationToken loginToken =
                    new UsernamePasswordAuthenticationToken(login, password);
            return getAuthenticationManager().authenticate(loginToken);

        }catch (IndexOutOfBoundsException e) {
            throw new AuthenticationCredentialsNotFoundException("Wrong header format..");
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = adminService.loadUserByUsername(((Admin)authentication.getPrincipal()).getUsername());

        final Admin authenticatedUser = new Admin(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        final AdminAuthentication userAuthentication = new AdminAuthentication(authenticatedUser);

        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
