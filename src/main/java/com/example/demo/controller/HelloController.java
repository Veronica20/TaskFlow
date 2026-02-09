package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.HelloService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }


    @PostMapping("/hello9")
    public Optional<User> hello() {
        return this.helloService.getGreeting();
    }
}