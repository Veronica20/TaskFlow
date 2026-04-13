package com.example.demo.service;

import com.example.demo.entity.LoginAttempt;
import com.example.demo.repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final LoginAttemptRepository repository;

    @Async
    public void logAttempt(String email, boolean success, String ipAddress) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setEmail(email);
        attempt.setSuccess(success);
        attempt.setIpAddress(ipAddress);
        attempt.setTimestamp(LocalDateTime.now());

        repository.save(attempt);

        System.out.println("Logged login attempt in thread: " + Thread.currentThread());
    }
}