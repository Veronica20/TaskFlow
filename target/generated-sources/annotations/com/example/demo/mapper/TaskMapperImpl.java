package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.dto.TaskUpdateRequestDto;
import com.example.demo.entity.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-30T22:23:50+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(TaskCreateRequestDto dto) {

        Task task = new Task();

        if ( dto != null ) {
            task.setTitle( dto.getTitle() );
            task.setShortDescription( dto.getShortDescription() );
            task.setDescription( dto.getDescription() );
            task.setDeadline( dto.getDeadline() );
            task.setStatus( dto.getStatus() );
            task.setPriority( dto.getPriority() );
        }

        return task;
    }

    @Override
    public TaskCreateRequestDto toDto(Task entity) {

        TaskCreateRequestDto taskCreateRequestDto = new TaskCreateRequestDto();

        if ( entity != null ) {
            taskCreateRequestDto.setTitle( entity.getTitle() );
            taskCreateRequestDto.setShortDescription( entity.getShortDescription() );
            taskCreateRequestDto.setDescription( entity.getDescription() );
            taskCreateRequestDto.setDeadline( entity.getDeadline() );
            taskCreateRequestDto.setStatus( entity.getStatus() );
            taskCreateRequestDto.setPriority( entity.getPriority() );
        }

        return taskCreateRequestDto;
    }

    @Override
    public void updateTask(TaskUpdateRequestDto dto, Task entity) {

        if ( dto != null ) {
            if ( dto.getTitle() != null ) {
                entity.setTitle( dto.getTitle() );
            }
            if ( dto.getShortDescription() != null ) {
                entity.setShortDescription( dto.getShortDescription() );
            }
            if ( dto.getDescription() != null ) {
                entity.setDescription( dto.getDescription() );
            }
            if ( dto.getDeadline() != null ) {
                entity.setDeadline( dto.getDeadline() );
            }
            if ( dto.getStatus() != null ) {
                entity.setStatus( dto.getStatus() );
            }
            if ( dto.getPriority() != null ) {
                entity.setPriority( dto.getPriority() );
            }
        }
    }
}
