package com.example.demo.validation;

import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, UUID> {

    private final UserRepository userRepository;

    public ExistingUserIdValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return userRepository.existsById(value);
    }
}
