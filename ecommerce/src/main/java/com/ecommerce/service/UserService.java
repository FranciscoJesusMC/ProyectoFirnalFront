package com.ecommerce.service;

import com.ecommerce.dto.request.UserRequestDTO;
import com.ecommerce.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO updateUser(UserRequestDTO userRequestDTO,Long id);

    void deleteUser(Long id);
}
