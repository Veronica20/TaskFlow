package com.example.demo.mapper;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface RegisterMapper {

    @Mapping(target = "deleted", ignore = true)
    User toEntity(RegisterRequest dto);
}
