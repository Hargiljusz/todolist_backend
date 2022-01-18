package com.iwaniuk.todolist_auth.controllers;

import com.iwaniuk.todolist_auth.config.RepoInit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    private final RepoInit repoInit;

    public HelloController(RepoInit repoInit) {
        this.repoInit = repoInit;
    }

    @GetMapping("/1")
    public String hello1(){
        return "Hello1";
    }

    @GetMapping("/2")
    public String hello2(){
        return "Hello2";
    }


    @GetMapping("/init")
    public String init(){
        repoInit.initDB();
        return "Hello2";
    }
}
