package com.example.demo.security;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.trimToNull;

@Component
public class CurrentTaskResolver implements HandlerMethodArgumentResolver {

    private final TaskRepository taskRepository;

    public CurrentTaskResolver(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Task.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Map<String, String> uriVariables =
                (Map<String, String>) webRequest.getAttribute(
                        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                        RequestAttributes.SCOPE_REQUEST
                );

        if (uriVariables == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is missing");
        }

        String taskIdParam = trimToNull(uriVariables.get("task"));
        if (taskIdParam == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is missing");
        }

        UUID taskId;
        try {
            taskId = UUID.fromString(taskIdParam);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task must be a valid UUID");
        }

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
