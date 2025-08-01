package com.rentahall.userservice.service;


import com.rentahall.userservice.dto.UserDTO;
import com.rentahall.userservice.entity.User;

import java.util.List;

public interface UserService {
    UserDTO register(User user);
    UserDTO findByEmail(String email);
    List<UserDTO> getAllUsers();
}