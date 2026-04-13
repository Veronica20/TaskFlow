package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LoginAttempt {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private boolean success;
    private String ipAddress;
    private LocalDateTime timestamp;
}
