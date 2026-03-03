package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface TaskMapper {
    Task toEntity(TaskCreateRequestDto dto);

    TaskCreateRequestDto toDto(Task entity);

    default TaskResponseDto toResponse(Task entity) {
        if (entity == null) {
            return null;
        }

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDeadline(entity.getDeadline());
        dto.setStatus(entity.getStatus());

        List<User> users = entity.getUsers() == null ? Collections.emptyList() : new ArrayList<>(entity.getUsers());
        dto.setUsers(users.stream()
                .map(u -> new TaskResponseDto.UserSummary(u.getId(), u.getEmail()))
                .collect(Collectors.toList()));

        return dto;
    }
}
