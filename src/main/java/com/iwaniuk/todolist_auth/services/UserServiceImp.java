package com.iwaniuk.todolist_auth.services;

import com.iwaniuk.todolist_auth.exceptions.PermissionDenied;
import com.iwaniuk.todolist_auth.exceptions.UserExistException;
import com.iwaniuk.todolist_auth.exceptions.UserNotFound;
import com.iwaniuk.todolist_auth.models.Role;
import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.models.UserRegister;
import com.iwaniuk.todolist_auth.repositories.RoleRepository;
import com.iwaniuk.todolist_auth.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFound::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
    }

    @Override
    public User register(UserRegister userRegister) {
        if(userRepository.findByEmail(userRegister.getEmail()).isPresent()){
            throw new UserExistException();
        }
        Role user_role = roleRepository.findByRole("ROLE_USER");
        User user = new User(
                userRegister.getEmail(),
                userRegister.getName(),
                userRegister.getSurname(),
                userRegister.getPhoneNumber(),
                passwordEncoder.encode(userRegister.getPassword()),
                Arrays.asList(user_role)
        );
        return userRepository.save(user);
    }

    @Override
    public void delete(String userId) {
       userRepository.deleteById(userId);
    }

    @Override
    public User update(UserRegister userRegister) {
        User user = new User(
                userRegister.getName(),
                userRegister.getSurname(),
                userRegister.getPhoneNumber()
        );
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByEmail(username);

        Set<GrantedAuthority> grantedAuthorities = user
                        .getRoles()
                        .stream()
                        .map(r->new SimpleGrantedAuthority(r.getRole()))
                        .collect(Collectors.toSet());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                 user.getEmail()
                ,user.getPassword()
                ,true
                ,true
                ,true
                ,true
                ,grantedAuthorities);

        return  userDetails;
        }
}

