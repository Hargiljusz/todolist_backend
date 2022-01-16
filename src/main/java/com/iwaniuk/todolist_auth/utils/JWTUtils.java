package com.iwaniuk.todolist_auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.iwaniuk.todolist_auth.models.RefreshResponse;
import com.iwaniuk.todolist_auth.models.Role;
import com.iwaniuk.todolist_auth.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUtils {
    private final String jwtSecret;
    private final String refreshToken;
    private final UserService userService;

    public JWTUtils(@Value("${token.jwt.secret}") String jwtSecret, @Value("${token.refresh.secret}") String refreshToken, UserService userService) {
        this.jwtSecret = jwtSecret;
        this.refreshToken = refreshToken;
        this.userService = userService;
    }

    public String generateJWT(String email, List<String> roles){
        var token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*30))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("roles", roles)
                .withIssuer("com.iwaniuk")
                .sign(Algorithm.HMAC256(jwtSecret));

        return token;
    }

    public DecodedJWT verifyJWT(String jwt){
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                        .build()
                        .verify(jwt);
    }

    public String generateRefreshToken(String id){
        var token = JWT.create()
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24*7))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer("com.iwaniuk")
                .sign(Algorithm.HMAC256(refreshToken));
        return token;
    }

    public RefreshResponse checkRefreshToken(String token) throws Exception{
        var userId =  JWT.require(Algorithm.HMAC256(refreshToken))
                .build()
                .verify(token).getSubject();
        var user = userService.getUserById(userId);
        var jwt = generateJWT(user.getEmail(),user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));
        return new RefreshResponse(jwt,userId);
    }
}
