package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateRequestDto;
import com.example.demo.entity.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-25T14:27:09+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 22 (Oracle Corporation)"
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
}
