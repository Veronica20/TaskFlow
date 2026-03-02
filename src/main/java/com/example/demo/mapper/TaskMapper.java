package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface TaskMapper {
    Task toEntity(TaskCreateRequestDto dto);

    TaskCreateRequestDto toDto(Task entity);

    TaskResponseDto toResponse(Task entity);
}
