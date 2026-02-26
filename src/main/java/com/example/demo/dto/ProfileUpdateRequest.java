package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
}
