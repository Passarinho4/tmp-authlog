package pl.com.tegess.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;


@Configuration
@Import(SpringSecurityConfig.class)
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private AdminService adminService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminService);
    }
}