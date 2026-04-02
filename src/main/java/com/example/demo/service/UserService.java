package com.example.demo.service;

import com.example.demo.dto.PreferencesUpdateRequest;
import com.example.demo.dto.ProfileUpdateRequest;
import com.example.demo.dto.UserCreateRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateRequestDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAddresses;
import com.example.demo.entity.UserPreferences;
import com.example.demo.entity.UserProfile;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AvatarStorageService avatarStorageService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       AvatarStorageService avatarStorageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.avatarStorageService = avatarStorageService;
    }

    public Optional<User> getGreeting() {
        return this.userRepository.findAll().stream().findFirst();
    }

    public Optional<User> getUserById(UUID id) {
        return this.userRepository.findById(id);
    }

    public UserResponseDto saveUser(UserCreateRequestDto userCreateRequestDto) {

        User user = userMapper.toEntity(userCreateRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    public UserResponseDto updateUser(User user, UserUpdateRequestDto userUpdateRequestDto, MultipartFile avatar) {

        userMapper.updateUser(userUpdateRequestDto, user);

        ProfileUpdateRequest profileUpdate = userUpdateRequestDto.getProfile();
        if (profileUpdate != null) {
            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            userMapper.updateProfile(profileUpdate, profile);
        }

        PreferencesUpdateRequest preferencesUpdate = userUpdateRequestDto.getPreferences();
        if (preferencesUpdate != null) {
            UserPreferences preferences = user.getPreferences();
            if (preferences == null) {
                preferences = new UserPreferences();
                preferences.setUser(user);
                user.setPreferences(preferences);
            }
            userMapper.updatePreferences(preferencesUpdate, preferences);
        }

        if (userUpdateRequestDto.getAddresses() != null) {
            List<com.example.demo.dto.AddressRequest> incoming = userUpdateRequestDto.getAddresses();
            Map<String, com.example.demo.dto.AddressRequest> incomingByZip = new LinkedHashMap<>();
            for (com.example.demo.dto.AddressRequest addressRequest : incoming) {
                String zipCode = addressRequest.getZipCode();
                if (incomingByZip.containsKey(zipCode)) {
                    throw new IllegalArgumentException("duplicate zipCode in addresses: " + zipCode);
                }
                incomingByZip.put(zipCode, addressRequest);
            }

            List<UserAddresses> addresses = user.getAddresses();
            Map<String, UserAddresses> existingByZip = addresses.stream()
                    .collect(Collectors.toMap(UserAddresses::getZipCode, address -> address));

            for (Map.Entry<String, com.example.demo.dto.AddressRequest> entry : incomingByZip.entrySet()) {
                String zipCode = entry.getKey();
                com.example.demo.dto.AddressRequest request = entry.getValue();
                UserAddresses address = existingByZip.get(zipCode);
                if (address == null) {
                    address = new UserAddresses();
                    address.setUser(user);
                    addresses.add(address);
                }
                userMapper.updateAddress(request, address);
            }

            Set<String> incomingZips = incomingByZip.keySet();
            addresses.removeIf(address -> !incomingZips.contains(address.getZipCode()));
        }

        if (avatar != null && !avatar.isEmpty()) {
            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            String previous = profile.getAvatarUrl();
            try {
                String url = avatarStorageService.store(avatar, user.getId());
                profile.setAvatarUrl(url);
                avatarStorageService.deleteIfExists(previous);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to store avatar", e);
            }
        }

        User saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    public Page<UserResponseDto> getUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    public UserResponseDto toUserResponse(User user) {
        return userMapper.toResponse(user);
    }
}
