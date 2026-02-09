package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank
    @Email
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
    private List<AddressRequest> addresses;

    @Valid
    private PreferencesRequest preferences;

}
