package com.example.demo.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequestDto {

    private String email;
    private String password;

    @Valid
    private ProfileUpdateRequest profile;

    @Valid
    private PreferencesUpdateRequest preferences;

    @Valid
    private List<AddressRequest> addresses;
}
