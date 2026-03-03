package com.example.demo.dto;

import com.example.demo.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private UUID id;
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskStatus status;

    private java.util.List<UserSummary> users;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private UUID userId;
        private String userName;
    }
}
