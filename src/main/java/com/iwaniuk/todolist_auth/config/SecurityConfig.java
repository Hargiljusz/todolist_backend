package com.iwaniuk.todolist_auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwaniuk.todolist_auth.services.UserService;
import com.iwaniuk.todolist_auth.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final JWTUtils jwtUtils;
    private final UserService userService;

    public SecurityConfig(ObjectMapper objectMapper, JWTUtils jwtUtils, UserService userService) {
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(new AuthorizationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/api/hello/2","/api/hello/init","/api/auth/**").permitAll()
                .antMatchers("/api/hello/1").hasRole("ADMIN")
                .antMatchers("/api/todo/**","/api/auth/logout").authenticated()
                .antMatchers("/api/user/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(objectMapper,jwtUtils,userService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/singIn");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return  jwtAuthenticationFilter;
    }
}
