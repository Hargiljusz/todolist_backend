package com.iwaniuk.todolist_auth.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.iwaniuk.todolist_auth.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;


public class AuthorizationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JWTUtils jwtUtils;
    Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);


    public AuthorizationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/")){
            var requestParams = request.getQueryString()==null?"":"?"+request.getQueryString();
            logger.info("Request: "+request.getRequestURL().toString()+requestParams+", method: "+request.getMethod()+", body size: "+request.getHeader(HttpHeaders.CONTENT_LENGTH));
        }

        if (!request.getServletPath().contains("/api/auth")) {
            UsernamePasswordAuthenticationToken authentication = null;
            try{
                 authentication = getAuthentication(request);
            }catch (JWTVerificationException ex){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }

            if(authentication!=null){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        logger.info("Token: "+token);

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            var jwtClaims = jwtUtils.verifyJWT(token.replace(TOKEN_PREFIX,""));
            var jwtRoles = jwtClaims.getClaim("roles").asList(String.class);
            Set<GrantedAuthority> grantedAuthorities = jwtRoles
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
            return new UsernamePasswordAuthenticationToken(jwtClaims.getSubject(),null,grantedAuthorities);
        }
        return  null;
    }
}

