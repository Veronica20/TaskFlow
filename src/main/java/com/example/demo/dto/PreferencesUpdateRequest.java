package com.example.demo.dto;

import com.example.demo.entity.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferencesUpdateRequest {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Language language;
}
