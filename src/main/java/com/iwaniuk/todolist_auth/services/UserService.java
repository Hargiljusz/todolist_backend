package com.iwaniuk.todolist_auth.services;

import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.models.UserRegister;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface UserService extends UserDetailsService {
    User getUserById(String userId);

    User getUserByEmail(String email);
    User register(UserRegister userRegister);
    void delete(String userId);
    User update(UserRegister userRegister);
}
