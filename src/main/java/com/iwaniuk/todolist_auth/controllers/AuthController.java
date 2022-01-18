package com.iwaniuk.todolist_auth.controllers;

import com.iwaniuk.todolist_auth.models.RefreshResponse;
import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.models.UserRegister;
import com.iwaniuk.todolist_auth.services.UserService;
import com.iwaniuk.todolist_auth.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private  final UserService userService;
    private final JWTUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, JWTUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/singUp")
    public ResponseEntity<User> singUp(@RequestBody UserRegister user){
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshJWT(@CookieValue("refreshToken") String rToken){
        logger.info("Refresh Token: "+rToken);
        try{
            var resMsg = jwtUtils.checkRefreshToken(rToken);
            return ResponseEntity.ok(resMsg);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response){
        response.addCookie(logoutCookie());
        return ResponseEntity.noContent().build();
    }
    private Cookie logoutCookie() {
        Cookie cookie = new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
