package com.rentahall.authservice.grpc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public RegisterUserResponse registerUser(String name, String email, String hashedPassword, String phone, Role role) {
        RegisterUserRequest request = RegisterUserRequest.newBuilder()
                .setName(name)
                .setEmail(email)
                .setPassword(hashedPassword)
                .setPhone(phone)
                .setRole(role != null ? role : Role.CLIENT)
                .build();

        try {
            return stub.registerUser(request);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to register user via gRPC: " + ex.getMessage(), ex);
        }
    }
}