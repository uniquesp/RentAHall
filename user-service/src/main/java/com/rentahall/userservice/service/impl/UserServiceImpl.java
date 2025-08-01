package com.rentahall.userservice.service.impl;

import com.rentahall.userservice.dto.UpdateUserDTO;
import com.rentahall.userservice.dto.UserDTO;
import com.rentahall.userservice.entity.User;
import com.rentahall.userservice.repository.UserRepository;
import com.rentahall.userservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDTO)
                .orElse(null);
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return toDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserDTO updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updateDto.getName() != null) user.setName(updateDto.getName());
        if (updateDto.getPhone() != null) user.setPhone(updateDto.getPhone());
        if (updateDto.getPasswordHash() != null) user.setPasswordHash(updateDto.getPasswordHash());

        return toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();
    }

}
