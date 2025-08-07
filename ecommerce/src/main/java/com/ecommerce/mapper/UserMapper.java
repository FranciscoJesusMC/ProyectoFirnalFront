package com.ecommerce.mapper;

import com.ecommerce.dto.request.UserRequestDTO;
import com.ecommerce.dto.response.UserResponseDTO;
import com.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toDTO(User user);
}
