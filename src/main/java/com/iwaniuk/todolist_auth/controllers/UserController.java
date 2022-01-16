package com.iwaniuk.todolist_auth.controllers;

import com.iwaniuk.todolist_auth.exceptions.PermissionDenied;
import com.iwaniuk.todolist_auth.exceptions.UserNotFound;
import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.models.UserRegister;
import com.iwaniuk.todolist_auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id){
        if(checkPermission(id)){
            var user = userService.getUserById(id);
            user.setPassword("");
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody UserRegister userRegister){
        if(checkPermission(id)){
            return ResponseEntity.ok(userService.update(userRegister));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable String id){
        if(checkPermission(id)){
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private boolean checkPermission(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userContext = userService.getUserByEmail(authentication.getName());

        return userContext.getRoles().stream().anyMatch(r->r.getRole().equals("ROLE_ADMIN")) || userContext.getId().equals(userId);
    }


}
