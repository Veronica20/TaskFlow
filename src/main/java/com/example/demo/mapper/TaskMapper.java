package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateRequestDto;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface TaskMapper {
    @Mapping(target = "users", ignore = true)
    Task toEntity(TaskCreateRequestDto dto);

    @Mapping(target = "users", ignore = true)
    TaskCreateRequestDto toDto(Task entity);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "users", ignore = true)
    void updateTask(TaskUpdateRequestDto dto, @MappingTarget Task entity);

    default TaskResponseDto toResponse(Task entity) {
        if (entity == null) {
            return null;
        }

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setShortDescription(entity.getShortDescription());
        dto.setDescription(entity.getDescription());
        dto.setDeadline(entity.getDeadline());
        dto.setStatus(entity.getStatus());
        dto.setPriority(entity.getPriority());

        List<User> users = entity.getUsers() == null ? Collections.emptyList() : new ArrayList<>(entity.getUsers());
        dto.setUsers(users.stream()
                .map(u -> new TaskResponseDto.UserSummary(u.getId(), u.getEmail()))
                .collect(Collectors.toList()));

        return dto;
    }
}
