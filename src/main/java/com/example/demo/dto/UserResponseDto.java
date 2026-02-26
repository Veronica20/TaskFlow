package com.example.demo.dto;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private Role role;
    private ProfileResponse profile;
    private List<AddressResponse> addresses;
    private PreferencesResponse preferences;
}
