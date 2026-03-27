package com.example.demo.mapper;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T11:43:49+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class RegisterMapperImpl implements RegisterMapper {

    @Override
    public User toEntity(RegisterRequest dto) {

        User user = new User();

        if ( dto != null ) {
            user.setEmail( dto.getEmail() );
            user.setPassword( dto.getPassword() );
        }

        return user;
    }
}
