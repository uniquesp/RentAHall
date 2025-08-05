package com.rentahall.userservice.service;


import com.rentahall.userservice.dto.UpdateUserDTO;
import com.rentahall.userservice.dto.UserDTO;
import com.rentahall.userservice.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO findByEmail(String email);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    UserDTO createUser(User user);

    List<UserDTO> getUsersByRole(User.Role role);

    boolean existsByEmail(String email);

    boolean validateUserCredentials(String email, String password);
}