package com.example.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(java.util.UUID id) {
        super("User not found with id " + id);
    }
}
