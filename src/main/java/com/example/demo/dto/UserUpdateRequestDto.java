package com.example.demo.dto;

import com.example.demo.entity.UserStatus;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequestDto {

    private String email;
    private String password;
    private UserStatus status;

    @Valid
    private ProfileUpdateRequest profile;

    @Valid
    private PreferencesUpdateRequest preferences;

    @Valid
    private List<AddressRequest> addresses;
}
