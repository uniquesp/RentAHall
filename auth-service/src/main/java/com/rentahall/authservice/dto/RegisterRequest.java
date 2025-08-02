package com.rentahall.authservice.dto;

import com.rentahall.authservice.grpc.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    @Pattern(regexp = "\\d{10}")
    private String phone;

    @NotNull
    private Role role;
}