package com.iwaniuk.todolist_auth.controllers;

import com.iwaniuk.todolist_auth.config.RepoInit;
import com.iwaniuk.todolist_auth.models.Stats;
import com.iwaniuk.todolist_auth.repositories.ToDoItemRepository;
import com.iwaniuk.todolist_auth.repositories.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/admin")
public class AdminController {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final ToDoItemRepository toDoItemRepository;
    private final RepoInit repoInit;

    public AdminController(ApplicationContext appl, UserRepository userRepository, ToDoItemRepository toDoItemRepository, RepoInit repoInit) {
        this.applicationContext = appl;
        this.userRepository = userRepository;
        this.toDoItemRepository = toDoItemRepository;
        this.repoInit = repoInit;
    }

    @GetMapping("/stats")
    public ResponseEntity<Stats> getStats(){
        var totalBeans = applicationContext.getBeanDefinitionCount();
        var totalUsers = userRepository.count();
        var totalToDos = toDoItemRepository.count();
        long timeJWT = 30;
        long timeRT = 60*60*24*7;
        return ResponseEntity.ok(new Stats(totalBeans,totalUsers,totalToDos,timeJWT,timeRT));
    }

    @GetMapping("initDB")
    public ResponseEntity initDB(){
        repoInit.initDB();
        return ResponseEntity.noContent().build();
    }

}
