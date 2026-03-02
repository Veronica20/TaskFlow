package com.example.demo.controller;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

    @PostMapping(
            value ="/api/tasks",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateRequestDto task) {

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

}
