package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private Role role;
    private ProfileRequest profile;
    private List<AddressRequest> addresses;
    private PreferencesRequest preferences;
}
