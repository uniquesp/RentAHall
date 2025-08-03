package com.rentahall.userservice.grpc;


import com.rentahall.userservice.entity.User;
import com.rentahall.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        // Map gRPC role enum to entity enum
        User.Role userRole = switch (request.getRole()) {
            case OWNER -> User.Role.OWNER;
            case CLIENT -> User.Role.CLIENT;
            case ADMIN -> User.Role.ADMIN;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid role");
        };

        // Create and save User
        User user = User.builder()
                .id(UUID.randomUUID()) // UUID ID
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPassword()) // Already hashed in auth-service
                .phone(request.getPhone())
                .role(userRole)
                .build();

        User savedUser = userRepository.save(user);

        // Send UUID string back to auth-service
        RegisterUserResponse response = RegisterUserResponse.newBuilder()
                .setMessage(savedUser.getId().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
