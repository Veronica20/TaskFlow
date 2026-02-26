package com.example.demo.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import com.example.demo.entity.User;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Invalid request");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSort(PropertyReferenceException ex,
                                                                 HttpServletRequest request) {
        String message = "Unknown sort property: " + ex.getPropertyName();
        List<Map<String, String>> errors = List.of(Map.of(
                "field", "sort",
                "message", message
        ));

        Map<String, Object> body = buildValidationBody(errors, request.getRequestURI());
        body.put("allowedSort", getAllowedSortFields(User.class));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        Map<String, Object> body = buildValidationBody(errors, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, Object>> handleValidationErrors(Exception ex,
                                                                      HttpServletRequest request) {
        List<Map<String, String>> errors;

        if (ex instanceof ConstraintViolationException cve) {
            errors = cve.getConstraintViolations()
                    .stream()
                    .map(violation -> Map.of(
                            "field", violation.getPropertyPath().toString(),
                            "message", violation.getMessage()
                    ))
                    .collect(Collectors.toList());
        } else {
            errors = buildJsonParseErrors((HttpMessageNotReadableException) ex);
        }

        Map<String, Object> body = buildValidationBody(errors, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private Map<String, Object> buildValidationBody(List<Map<String, String>> errors, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");
        body.put("path", path);
        body.put("errors", errors);
        return body;
    }

    private List<Map<String, String>> buildJsonParseErrors(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";
        String field = "body";

        if (ex.getCause() instanceof MismatchedInputException mie && !mie.getPath().isEmpty()) {
            field = mie.getPath().get(0).getFieldName();
            message = "Wrong JSON type for field: " + field;
        }

        return List.of(Map.of(
                "field", field,
                "message", message
        ));
    }

    private List<String> getAllowedSortFields(Class<?> type) {
        Set<String> fields = new LinkedHashSet<>();
        collectSortableFields(type, "", fields, 1);
        return List.copyOf(fields);
    }

    private void collectSortableFields(Class<?> type,
                                       String prefix,
                                       Set<String> fields,
                                       int depth) {
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            if ("password".equals(field.getName())) {
                continue;
            }
            if (isAssociationField(field)) {
                if (depth > 0) {
                    collectSortableFields(field.getType(),
                            prefix + field.getName() + ".",
                            fields,
                            depth - 1);
                }
                continue;
            }
            if (!isSortableType(field.getType())) {
                continue;
            }
            fields.add(prefix + field.getName());
        }
    }

    private boolean isAssociationField(Field field) {
        return field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(ManyToMany.class);
    }

    private boolean isSortableType(Class<?> type) {
        return type.isPrimitive()
                || type.equals(String.class)
                || type.equals(UUID.class)
                || type.isEnum()
                || Number.class.isAssignableFrom(type)
                || type.equals(Boolean.class)
                || java.time.temporal.Temporal.class.isAssignableFrom(type);
    }
}
