package com.example.demo.mapper;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface RegisterMapper {

    User toEntity(RegisterRequest dto);
}
