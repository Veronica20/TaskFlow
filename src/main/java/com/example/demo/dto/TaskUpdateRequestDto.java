package com.example.demo.dto;

import com.example.demo.entity.TaskPriority;
import com.example.demo.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequestDto {
    private String title;

    private String shortDescription;

    private String description;

    private LocalDate deadline;

    private TaskStatus status;

    private TaskPriority priority;

    private List<UUID> users;
}
