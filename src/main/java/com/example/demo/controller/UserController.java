package com.example.demo.controller;

import com.example.demo.dto.UserCreateRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateRequestDto;
import com.example.demo.entity.User;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
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
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {

       UserResponseDto userResponseDto = userService.saveUser(userCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseDto);
    }

    @PatchMapping(
            value = "/users/{user}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable
            @CurrentUser User user,
            @RequestBody UserUpdateRequestDto userUpdateRequestDto
    ) {
        UserResponseDto userResponseDto = userService.updateUser(
                user,
                userUpdateRequestDto,
                null
        );

        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * Send {@code multipart/form-data} with optional part {@code user} (JSON, content-type application/json)
     * and optional part {@code avatar} (image file: jpeg, png, gif, webp).
     */
    @PatchMapping(
            value = "/users/{user}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDto> updateUserWithAvatar(
            @PathVariable
            @CurrentUser User user,
            @RequestPart(value = "user", required = false) @Valid UserUpdateRequestDto userUpdateRequestDto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        UserUpdateRequestDto dto = userUpdateRequestDto != null ? userUpdateRequestDto : new UserUpdateRequestDto();
        UserResponseDto userResponseDto = userService.updateUser(user, dto, avatar);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/users")
    public Page<UserResponseDto> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }


    @GetMapping("/users/{user}")
    public ResponseEntity<UserResponseDto> getUser(@CurrentUser User user) {
        UserResponseDto userResponseDto = userService.toUserResponse(user);

        return ResponseEntity.ok(userResponseDto);
    }

}
