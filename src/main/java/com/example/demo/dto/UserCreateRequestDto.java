package com.example.demo.dto;

import com.example.demo.entity.Role;
import com.example.demo.validation.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

    @NotBlank
    @Email
    @UniqueEmail
    private String email;

    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    @NotNull
    private Role role;

    @Valid
    @NotNull
    private ProfileRequest profile;


    @Valid
    @Size(min = 1)
    @NotEmpty
    private List<AddressRequest> addresses;


    @Valid
    private PreferencesRequest preferences;

}
