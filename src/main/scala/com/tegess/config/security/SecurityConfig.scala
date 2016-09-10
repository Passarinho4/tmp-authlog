package com.tegess.config.security

import com.tegess.persistance.MongoConfig
import com.tegess.persistance.service.admin.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration, Import}
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@Import(Array(classOf[MongoConfig]))
class SecurityConfig extends WebSecurityConfigurerAdapter(true) {

  final private val SECRET: String = "fe9c0838-963f-4c91-b133-bf56b1a8fb79"

  @Autowired
  var adminService:AdminService = _

  override def configure(http: HttpSecurity) {
    http
      .exceptionHandling.and
      .anonymous.and
      .servletApi.and
      .authorizeRequests
      .antMatchers("/").authenticated
      .antMatchers("/api/test2").permitAll()
      .antMatchers("/api/loginAdmin").permitAll
      .antMatchers("/api/login/facebook").permitAll
      .antMatchers("/api/login/credentials").permitAll
      .antMatchers("/api/logged/facebook").permitAll
      .antMatchers(HttpMethod.POST, "/api/applications/*/users").permitAll
      .antMatchers(HttpMethod.POST, "/api/install").permitAll
      .anyRequest.authenticated.and
      .addFilterBefore(statelessLoginFilter, classOf[UsernamePasswordAuthenticationFilter])
      .addFilterBefore(statelessAuthenticationFilter, classOf[UsernamePasswordAuthenticationFilter])
      .asInstanceOf[HttpSecurity]
      .logout().logoutRequestMatcher(new AntPathRequestMatcher("/api/logoutAdmin"))
      .logoutSuccessHandler(new LogoutSuccessHandler("http://localhost:8090/")).and
      .headers().cacheControl()
  }

  override protected def configure(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder)
  }

  @Bean def tokenAuthenticationService: TokenAuthenticationService = {
    new TokenAuthenticationService(SECRET, adminService)
  }

  @Bean def statelessAuthenticationFilter: StatelessAuthenticationFilter = {
    new StatelessAuthenticationFilter
  }

  @Bean
  def statelessLoginFilter: StatelessLoginFilter = {
    new StatelessLoginFilter("/api/loginAdmin", authenticationManager())
  }

  @Bean override def userDetailsService: AdminService = {
    adminService
  }

  @Bean
  override def authenticationManagerBean: AuthenticationManager = {
    super.authenticationManagerBean()
  }
}
