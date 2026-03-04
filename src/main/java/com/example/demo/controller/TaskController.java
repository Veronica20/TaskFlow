package com.example.demo.controller;

import com.example.demo.dto.TaskAssignUsersRequestDto;
import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PutMapping("/users/{userId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> assignTaskToUser(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        return ResponseEntity.ok(taskService.assignTaskToUser(taskId, userId));
    }

    @PutMapping(value = "/api/tasks/{taskId}/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TaskResponseDto> assignTaskToUsers(
            @PathVariable UUID taskId,
            @RequestBody TaskAssignUsersRequestDto request) {
        List<UUID> userIds = request == null || request.getUsers() == null
                ? List.of()
                : request.getUsers().stream().map(TaskAssignUsersRequestDto.UserRef::getUserId).toList();

        return ResponseEntity.ok(taskService.assignTaskToUsers(taskId, userIds));
    }

    @DeleteMapping("/api/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    public void getTasks(UUID taskId) {
        //todo
    }

    public void getTask(UUID taskId) {
        //todo
    }

    public void updateTask(UUID taskId) {
        //todo
    }
}
