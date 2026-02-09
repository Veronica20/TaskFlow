package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import com.example.demo.entity.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferencesRequest {

    private boolean emailNotifications;

    private boolean smsNotifications;

    @NotNull
    private Language language;
}
