package com.example.demo.mapper;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.dto.PreferencesRequest;
import com.example.demo.dto.PreferencesResponse;
import com.example.demo.dto.PreferencesUpdateRequest;
import com.example.demo.dto.ProfileRequest;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.ProfileUpdateRequest;
import com.example.demo.dto.UserCreateRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateRequestDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAddresses;
import com.example.demo.entity.UserPreferences;
import com.example.demo.entity.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "preferences", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    void updateUser(UserUpdateRequestDto dto, @MappingTarget User user);

    UserResponseDto toResponse(User user);

    UserProfile toEntity(ProfileRequest dto);

    UserPreferences toEntity(PreferencesRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfile(ProfileUpdateRequest dto, @MappingTarget UserProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePreferences(PreferencesUpdateRequest dto, @MappingTarget UserPreferences preferences);

    @Mapping(target = "primaryAddress", source = "primary")
    UserAddresses toEntity(AddressRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "primaryAddress", source = "primary")
    void updateAddress(AddressRequest dto, @MappingTarget UserAddresses address);

    ProfileResponse toResponse(UserProfile profile);

    PreferencesResponse toResponse(UserPreferences preferences);

    @Mapping(target = "primary", source = "primaryAddress")
    AddressResponse toResponse(UserAddresses address);

    List<AddressResponse> toResponse(List<UserAddresses> addresses);

    @AfterMapping
    default void linkUser(UserCreateRequestDto dto, @MappingTarget User user) {
        UserProfile profile = user.getProfile();
        if (profile != null) {
            profile.setUser(user);
        }

        UserPreferences preferences = user.getPreferences();
        if (preferences != null) {
            preferences.setUser(user);
        }

        List<UserAddresses> addresses = user.getAddresses();
        if (addresses != null) {
            for (UserAddresses address : addresses) {
                address.setUser(user);
            }
        }
    }
}
