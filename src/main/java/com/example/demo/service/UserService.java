package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateRequestDto;
import com.example.demo.dto.PreferencesRequest;
import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.AddressRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserPreferences;
import com.example.demo.entity.UserProfile;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getGreeting() {
        return this.userRepository.findById(1L);
    }

    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public UserResponseDto saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {

        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRole(userRequestDto.getRole());

        ProfileRequest profileRequest = userRequestDto.getProfile();
        if (profileRequest != null) {
            UserProfile profile = new UserProfile();
            profile.setFirstName(profileRequest.getFirstName());
            profile.setLastName(profileRequest.getLastName());
            profile.setBirthDate(profileRequest.getBirthDate());
            profile.setPhoneNumber(profileRequest.getPhoneNumber());
            profile.setUser(user);
            user.setProfile(profile);
        }

        PreferencesRequest preferencesRequest = userRequestDto.getPreferences();
        if (preferencesRequest != null) {
            UserPreferences preferences = new UserPreferences();
            preferences.setEmailNotifications(preferencesRequest.isEmailNotifications());
            preferences.setSmsNotifications(preferencesRequest.isSmsNotifications());
            preferences.setLanguage(preferencesRequest.getLanguage());
            preferences.setUser(user);
            user.setPreferences(preferences);
        }

        User saved = userRepository.save(user);

        return buildUserResponse(saved, userRequestDto.getAddresses());
    }

    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userUpdateRequestDto.getEmail() != null) {
            user.setEmail(userUpdateRequestDto.getEmail());
        }

        if (userUpdateRequestDto.getPassword() != null) {
            user.setPassword(userUpdateRequestDto.getPassword());
        }

        User saved = userRepository.save(user);

        return buildUserResponse(saved, null);
    }

    private UserResponseDto buildUserResponse(User user, List<AddressRequest> addresses) {
        ProfileRequest responseProfile = null;
        if (user.getProfile() != null) {
            responseProfile = new ProfileRequest();
            responseProfile.setFirstName(user.getProfile().getFirstName());
            responseProfile.setLastName(user.getProfile().getLastName());
            responseProfile.setBirthDate(user.getProfile().getBirthDate());
            responseProfile.setPhoneNumber(user.getProfile().getPhoneNumber());
        }

        PreferencesRequest responsePreferences = null;
        if (user.getPreferences() != null) {
            responsePreferences = new PreferencesRequest();
            responsePreferences.setEmailNotifications(user.getPreferences().isEmailNotifications());
            responsePreferences.setSmsNotifications(user.getPreferences().isSmsNotifications());
            responsePreferences.setLanguage(user.getPreferences().getLanguage());
        }

        return new UserResponseDto(
                user.getEmail(),
                user.getRole(),
                responseProfile,
                addresses,
                responsePreferences
        );
    }
}
