package com.example.demo.service;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class TaskService {

   private final TaskRepository taskRepository;
   private final UserRepository userRepository;
   private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto createTask(TaskCreateRequestDto taskCreateRequestDto) {

        Task task = taskRepository.save(taskMapper.toEntity(taskCreateRequestDto));

        return taskMapper.toResponse(task);
    }

    public TaskResponseDto assignTaskToUser(UUID taskId, UUID userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.getUsers().add(user);
        taskRepository.save(task);

        return taskMapper.toResponse(task);
    }

    public TaskResponseDto assignTaskToUsers(UUID taskId, List<UUID> userIds) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (userIds == null || userIds.isEmpty()) {
            return taskMapper.toResponse(task);
        }

        List<UUID> uniqueUserIds = userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<User> users = userRepository.findAllById(uniqueUserIds);
        if (users.size() != uniqueUserIds.size()) {
            throw new RuntimeException("One or more users not found");
        }

        task.getUsers().addAll(users);
        taskRepository.save(task);

        return taskMapper.toResponse(task);
    }

    public void deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    public TaskResponseDto toTaskResponse(Task task) {
        return taskMapper.toResponse(task);
    }

    public Page<TaskResponseDto> getTasks(Pageable pageable) {

        return taskRepository.findAll(pageable)
                .map(taskMapper::toResponse);
    }
}
