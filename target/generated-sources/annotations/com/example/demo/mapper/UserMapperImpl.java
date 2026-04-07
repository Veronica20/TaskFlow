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
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAddresses;
import com.example.demo.entity.UserPreferences;
import com.example.demo.entity.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-08T01:24:45+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateRequestDto dto) {

        User user = new User();

        if ( dto != null ) {
            user.setEmail( dto.getEmail() );
            user.setPassword( dto.getPassword() );
            user.setRole( dto.getRole() );
            user.setProfile( toEntity( dto.getProfile() ) );
            user.setPreferences( toEntity( dto.getPreferences() ) );
            user.setAddresses( addressRequestListToUserAddressesList( dto.getAddresses() ) );
        }

        linkUser( dto, user );

        return user;
    }

    @Override
    public void updateUser(UserUpdateRequestDto dto, User user) {

        if ( dto != null ) {
            if ( dto.getEmail() != null ) {
                user.setEmail( dto.getEmail() );
            }
            if ( dto.getPassword() != null ) {
                user.setPassword( dto.getPassword() );
            }
        }
    }

    @Override
    public UserResponseDto toResponse(User user) {

        UUID id = null;
        String email = null;
        Role role = null;
        ProfileResponse profile = null;
        List<AddressResponse> addresses = null;
        PreferencesResponse preferences = null;
        if ( user != null ) {
            id = user.getId();
            email = user.getEmail();
            role = user.getRole();
            profile = toResponse( user.getProfile() );
            addresses = toResponse( user.getAddresses() );
            preferences = toResponse( user.getPreferences() );
        }

        UserResponseDto userResponseDto = new UserResponseDto( id, email, role, profile, addresses, preferences );

        if ( user != null ) {
        }

        return userResponseDto;
    }

    @Override
    public UserProfile toEntity(ProfileRequest dto) {

        UserProfile userProfile = new UserProfile();

        if ( dto != null ) {
            userProfile.setFirstName( dto.getFirstName() );
            userProfile.setLastName( dto.getLastName() );
            userProfile.setBirthDate( dto.getBirthDate() );
            userProfile.setPhoneNumber( dto.getPhoneNumber() );
        }

        return userProfile;
    }

    @Override
    public UserPreferences toEntity(PreferencesRequest dto) {

        UserPreferences userPreferences = new UserPreferences();

        if ( dto != null ) {
            userPreferences.setEmailNotifications( dto.isEmailNotifications() );
            userPreferences.setSmsNotifications( dto.isSmsNotifications() );
            userPreferences.setLanguage( dto.getLanguage() );
        }

        return userPreferences;
    }

    @Override
    public void updateProfile(ProfileUpdateRequest dto, UserProfile profile) {

        if ( dto != null ) {
            if ( dto.getFirstName() != null ) {
                profile.setFirstName( dto.getFirstName() );
            }
            if ( dto.getLastName() != null ) {
                profile.setLastName( dto.getLastName() );
            }
            if ( dto.getBirthDate() != null ) {
                profile.setBirthDate( dto.getBirthDate() );
            }
            if ( dto.getPhoneNumber() != null ) {
                profile.setPhoneNumber( dto.getPhoneNumber() );
            }
        }
    }

    @Override
    public void updatePreferences(PreferencesUpdateRequest dto, UserPreferences preferences) {

        if ( dto != null ) {
            if ( dto.getEmailNotifications() != null ) {
                preferences.setEmailNotifications( dto.getEmailNotifications() );
            }
            if ( dto.getSmsNotifications() != null ) {
                preferences.setSmsNotifications( dto.getSmsNotifications() );
            }
            if ( dto.getLanguage() != null ) {
                preferences.setLanguage( dto.getLanguage() );
            }
        }
    }

    @Override
    public UserAddresses toEntity(AddressRequest dto) {

        UserAddresses userAddresses = new UserAddresses();

        if ( dto != null ) {
            userAddresses.setPrimaryAddress( dto.isPrimary() );
            userAddresses.setCountry( dto.getCountry() );
            userAddresses.setCity( dto.getCity() );
            userAddresses.setStreet( dto.getStreet() );
            userAddresses.setZipCode( dto.getZipCode() );
        }

        return userAddresses;
    }

    @Override
    public void updateAddress(AddressRequest dto, UserAddresses address) {

        if ( dto != null ) {
            address.setPrimaryAddress( dto.isPrimary() );
            if ( dto.getCountry() != null ) {
                address.setCountry( dto.getCountry() );
            }
            if ( dto.getCity() != null ) {
                address.setCity( dto.getCity() );
            }
            if ( dto.getStreet() != null ) {
                address.setStreet( dto.getStreet() );
            }
            if ( dto.getZipCode() != null ) {
                address.setZipCode( dto.getZipCode() );
            }
        }
    }

    @Override
    public ProfileResponse toResponse(UserProfile profile) {

        ProfileResponse profileResponse = new ProfileResponse();

        if ( profile != null ) {
            profileResponse.setId( profile.getId() );
            profileResponse.setFirstName( profile.getFirstName() );
            profileResponse.setLastName( profile.getLastName() );
            profileResponse.setBirthDate( profile.getBirthDate() );
            profileResponse.setPhoneNumber( profile.getPhoneNumber() );
            profileResponse.setAvatarUrl( profile.getAvatarUrl() );
        }

        return profileResponse;
    }

    @Override
    public PreferencesResponse toResponse(UserPreferences preferences) {

        PreferencesResponse preferencesResponse = new PreferencesResponse();

        if ( preferences != null ) {
            preferencesResponse.setEmailNotifications( preferences.isEmailNotifications() );
            preferencesResponse.setSmsNotifications( preferences.isSmsNotifications() );
            preferencesResponse.setLanguage( preferences.getLanguage() );
        }

        return preferencesResponse;
    }

    @Override
    public AddressResponse toResponse(UserAddresses address) {

        AddressResponse addressResponse = new AddressResponse();

        if ( address != null ) {
            if ( address.getPrimaryAddress() != null ) {
                addressResponse.setPrimary( address.getPrimaryAddress() );
            }
            addressResponse.setCountry( address.getCountry() );
            addressResponse.setCity( address.getCity() );
            addressResponse.setStreet( address.getStreet() );
            addressResponse.setZipCode( address.getZipCode() );
        }

        return addressResponse;
    }

    @Override
    public List<AddressResponse> toResponse(List<UserAddresses> addresses) {
        if ( addresses == null ) {
            return new ArrayList<AddressResponse>();
        }

        List<AddressResponse> list = new ArrayList<AddressResponse>( addresses.size() );
        for ( UserAddresses userAddresses : addresses ) {
            list.add( toResponse( userAddresses ) );
        }

        return list;
    }

    protected List<UserAddresses> addressRequestListToUserAddressesList(List<AddressRequest> list) {
        if ( list == null ) {
            return new ArrayList<UserAddresses>();
        }

        List<UserAddresses> list1 = new ArrayList<UserAddresses>( list.size() );
        for ( AddressRequest addressRequest : list ) {
            list1.add( toEntity( addressRequest ) );
        }

        return list1;
    }
}
