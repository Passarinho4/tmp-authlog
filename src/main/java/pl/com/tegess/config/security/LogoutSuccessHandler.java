package pl.com.tegess.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    // Just for setting the default target URL
    public LogoutSuccessHandler(String defaultTargetURL) {
        this.setRedirectStrategy((request, response, url) -> {

        });
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // do whatever you want
        super.onLogoutSuccess(request, response, authentication);
    }
}
