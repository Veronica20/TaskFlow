package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.mapper.RegisterMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RegisterMapper registerMapper;
    private final EmailService emailService;


    private final AuthenticationManager authenticationManager;

    public AuthResponse login(
            LoginRequest request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtService.generateToken(request.getEmail());

        log.info("Login success: {}", request.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = registerMapper.toEntity(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getEmail());

        log.info("Register success: {}", savedUser.getEmail());
        emailService.sendWelcomeEmail(user.getEmail());

        return new AuthResponse(token);
    }
}
