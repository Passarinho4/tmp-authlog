package pl.com.tegess.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String SECRET = "fe9c0838-963f-4c91-b133-bf56b1a8fb79";

    public SpringSecurityConfig() {
        super(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/api/install").permitAll()
                .antMatchers("/api/loginAdmin").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(statelessLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(statelessAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout().
                logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")).
                logoutSuccessHandler(new LogoutSuccessHandler("http://google.com")).and()
                .headers().cacheControl();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AdminService adminService() {
        return new AdminService();
    }

    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return new TokenAuthenticationService(SECRET, adminService());
    }

    @Bean
    public StatelessAuthenticationFilter statelessAuthenticationFilter() {
        return new StatelessAuthenticationFilter();
    }

    @Bean
    public StatelessLoginFilter statelessLoginFilter() throws Exception {
        return new StatelessLoginFilter("/api/loginAdmin", authenticationManager());
    }

    @Bean
    @Override
    public AdminService userDetailsService() {
        return adminService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
