package com.rentahall.userservice.service;


import com.rentahall.userservice.dto.UpdateUserDTO;
import com.rentahall.userservice.dto.UserDTO;
import com.rentahall.userservice.entity.User;

import java.util.List;

public interface UserService {
    UserDTO findByEmail(String email);
    List<UserDTO> getAllUsers();
    public UserDTO updateUser(Long id, UpdateUserDTO updateDto);
    public List<UserDTO> getUsersByRole(User.Role role);
    public UserDTO getUserById(Long id);
}