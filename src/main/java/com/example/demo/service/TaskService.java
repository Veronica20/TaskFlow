package com.example.demo.service;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.entity.Task;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

   private final TaskRepository taskRepository;
   private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto createTask(TaskCreateRequestDto taskCreateRequestDto) {

        Task task = taskRepository.save(taskMapper.toEntity(taskCreateRequestDto));

        return taskMapper.toResponse(task);
    }
}
