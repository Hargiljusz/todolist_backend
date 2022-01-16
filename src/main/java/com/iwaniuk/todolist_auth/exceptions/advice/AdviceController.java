package com.iwaniuk.todolist_auth.exceptions.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.iwaniuk.todolist_auth.exceptions.PermissionDenied;
import com.iwaniuk.todolist_auth.exceptions.ToDoItemNotFound;
import com.iwaniuk.todolist_auth.exceptions.UserExistException;
import com.iwaniuk.todolist_auth.exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(
            PermissionDenied.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> permission(RuntimeException ex){
        Map<String,String> msg = new HashMap<>();
        msg.put("msg",ex.getMessage());
        msg.put("type","PermissionException");
        msg.put("date", Instant.now().toString());
        return ok(msg);
    }

    @ExceptionHandler(
            UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> exist(RuntimeException ex){
        Map<String,String> msg = new HashMap<>();
        msg.put("msg",ex.getMessage());
        msg.put("type","PermissionException");
        msg.put("date", Instant.now().toString());
        return ok(msg);
    }
    @ExceptionHandler({
            UserNotFound.class,
            ToDoItemNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFound(RuntimeException ex){
        Map<String,String> msg = new HashMap<>();
        msg.put("msg",ex.getMessage());
        msg.put("type","PermissionException");
        msg.put("date", Instant.now().toString());
        return ok(msg);
    }

}
