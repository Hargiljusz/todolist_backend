package com.iwaniuk.todolist_auth.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwaniuk.todolist_auth.models.LoginModel;
import com.iwaniuk.todolist_auth.models.Role;
import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.services.UserService;
import com.iwaniuk.todolist_auth.utils.JWTUtils;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final JWTUtils jwtUtils;
    private final UserService userService;

    public JWTAuthenticationFilter( ObjectMapper objectMapper, JWTUtils jwtUtils, UserService userService) {
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            var body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            var loginModel = objectMapper.readValue(body, LoginModel.class);
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginModel.getEmail(),loginModel.getPassword()));
        } catch (IOException e) {
           throw new RuntimeException(e.getMessage()+":::"+"error when processing request body");
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetails principal = (UserDetails) authResult.getPrincipal();
        var email = principal.getUsername();
        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = userService.getUserByEmail(email);

        var jwt = jwtUtils.generateJWT(email,roles);
        var refreshToken = jwtUtils.generateRefreshToken(user.getId());
        var refreshCookie = generateRefreshCookie(refreshToken);

        var responseBody = generateResponseBody(user,jwt,refreshToken);
        response.setStatus(HttpStatus.OK.value());
        response.addCookie(refreshCookie);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().print(responseBody);
    }

    private Cookie generateRefreshCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(604800);
        return cookie;
    }

    private String generateResponseBody(User user, String jwt, String refreshToken) throws JsonProcessingException {
        Map<Object, Object> model = new HashMap<>();
        model.put("jwt",jwt);
        model.put("Bearer_token","Bearer "+jwt);
        model.put("username",user.getEmail());
        model.put("userID",user.getId());
        model.put("roles",user.getRoles().stream().map(Role::getRole));
        model.put("refreshToken",refreshToken);
        model.put("name",user.getName());

        return objectMapper.writeValueAsString(model);
    }

}
