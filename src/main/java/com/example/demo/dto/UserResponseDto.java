package com.example.demo.dto;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String email;
    private Role role;
    private UserStatus status;
    private ProfileResponse profile;
    private List<AddressResponse> addresses;
    private PreferencesResponse preferences;
}
