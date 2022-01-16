package com.iwaniuk.todolist_auth.config;

import com.iwaniuk.todolist_auth.models.Role;
import com.iwaniuk.todolist_auth.models.User;
import com.iwaniuk.todolist_auth.repositories.RoleRepository;
import com.iwaniuk.todolist_auth.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class RepoInit {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RepoInit(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder =passwordEncoder;
    }

    public void initDB(){
        if(roleRepository.count()==0){
            Role admin = roleRepository.save(new Role("ROLE_ADMIN"));
            Role moderator = roleRepository.save(new Role("ROLE_MODERATOR"));
            Role user = roleRepository.save(new Role("ROLE_USER"));

            roleRepository.saveAll(Arrays.asList(admin,moderator,user));

            User userA = new User("admin@gmail.com","Mariusz","Pudzianowski","111222333"
                    ,passwordEncoder.encode("test")
                    ,Arrays.asList(admin,moderator,user));

            User userM = new User("moderator@gmail.com","Michal","Wisniewski","999888777"
                    ,passwordEncoder.encode("test")
                    ,Arrays.asList(moderator,user));

            User userU = new User("user@gmail.com","Jan","Kowalski","999999999"
                    ,passwordEncoder.encode("test")
                    ,Arrays.asList(user));

            userRepository.saveAll(Arrays.asList(userA,userM,userU));
        }
    }
}
