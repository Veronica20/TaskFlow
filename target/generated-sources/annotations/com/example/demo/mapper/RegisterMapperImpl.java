package com.example.demo.mapper;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T19:47:33+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.10 (Eclipse Adoptium)"
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
