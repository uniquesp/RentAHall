package com.rentahall.authservice.service;

import com.rentahall.authservice.config.JwtService;
import com.rentahall.authservice.dto.AuthResponse;
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
}
