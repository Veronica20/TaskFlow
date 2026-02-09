package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_preferences")
public class UserPreferences {

    @Id
    @GeneratedValue
    private Long id;

    private boolean emailNotifications;
    private boolean smsNotifications;

    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
