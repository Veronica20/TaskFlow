package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.entity.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-03T22:06:23+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(TaskCreateRequestDto dto) {

        Task task = new Task();

        if ( dto != null ) {
            task.setTitle( dto.getTitle() );
            task.setDescription( dto.getDescription() );
            task.setDeadline( dto.getDeadline() );
            task.setStatus( dto.getStatus() );
        }

        return task;
    }

    @Override
    public TaskCreateRequestDto toDto(Task entity) {

        TaskCreateRequestDto taskCreateRequestDto = new TaskCreateRequestDto();

        if ( entity != null ) {
            taskCreateRequestDto.setTitle( entity.getTitle() );
            taskCreateRequestDto.setDescription( entity.getDescription() );
            taskCreateRequestDto.setDeadline( entity.getDeadline() );
            taskCreateRequestDto.setStatus( entity.getStatus() );
        }

        return taskCreateRequestDto;
    }
}
