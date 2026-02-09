package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelloService {

    private final UserRepository userRepository;

    public HelloService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getGreeting() {
        return this.userRepository.findById(1L);
    }
}
