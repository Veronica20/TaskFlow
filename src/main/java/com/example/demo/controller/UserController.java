package com.example.demo.controller;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateRequestDto;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            value = "/users",
            consumes = "application/json",
            produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {

       UserResponseDto userResponseDto = userService.saveUser(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseDto);
    }

    @PutMapping(
            value = "/users/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto userUpdateRequestDto
    ) {
        UserResponseDto userResponseDto = userService.updateUser(id, userUpdateRequestDto);

        return ResponseEntity.ok(userResponseDto);
    }

}
