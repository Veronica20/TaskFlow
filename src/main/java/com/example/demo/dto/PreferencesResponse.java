package com.example.demo.dto;

import com.example.demo.entity.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferencesResponse {
    private boolean emailNotifications;
    private boolean smsNotifications;
    private Language language;
}
