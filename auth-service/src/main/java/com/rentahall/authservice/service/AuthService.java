package com.rentahall.authservice.service;

import com.rentahall.authservice.config.JwtService;
import com.rentahall.authservice.dto.AuthResponse;
import com.rentahall.authservice.dto.LoginRequestDTO;
import com.rentahall.authservice.dto.RegisterRequest;
import com.rentahall.authservice.grpc.UserGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserGrpcClient userGrpcClient;

    public AuthResponse register(RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        var response = userGrpcClient.registerUser(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                request.getPhone(),
                request.getRole()
        );

        String userId = response.getMessage();

        String token = jwtService.generateToken(
                userId,
                request.getEmail(),
                request.getRole().name()
        );

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequestDTO request) {
        var response = userGrpcClient.validateUser(
                request.getEmail(),
                request.getPassword()
        );

        if (!response.getValid()) {
            throw new RuntimeException("Invalid email or password");
        }

        String jwtToken = jwtService.generateToken(
                response.getUserId(),
                request.getEmail(),
                response.getRole().name()
        );

        return new AuthResponse(jwtToken);
    }
}