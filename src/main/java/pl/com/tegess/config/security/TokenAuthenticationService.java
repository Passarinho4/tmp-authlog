package pl.com.tegess.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import pl.com.tegess.domain.admin.Admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService(String secret, AdminService adminService) {
        tokenHandler = new TokenHandler(secret, adminService);
    }

    public void addAuthentication(HttpServletResponse response, AdminAuthentication adminAuthentication) {
        String username = adminAuthentication.getName();
        Collection<? extends GrantedAuthority> authorities = adminAuthentication.getAuthorities();

        response.addHeader("Access-Control-Expose-Headers", AUTH_HEADER_NAME);
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForAdmin(username, authorities));
    }

    public Authentication getAuthentication(HttpServletRequest request){
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if(token != null) {
            final Admin user = tokenHandler.parseUserFromToken(token);
            if(user != null) {
                return new AdminAuthentication(user);
            }
        }
        return null;
    }
}
