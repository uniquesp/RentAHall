package com.rentahall.userservice.grpc;


import com.rentahall.userservice.entity.User;
import com.rentahall.userservice.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        try {
            System.out.println("=== SERVER DEBUG: Received Request ===");
            System.out.println("Request: " + request);
            System.out.println("Name: " + request.getName());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Phone: " + request.getPhone());
            System.out.println("Role: " + request.getRole());

            // Validate request
            if (request.getName() == null || request.getName().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Name is required").asRuntimeException());
                return;
            }

            // Map gRPC role enum to entity enum
            User.Role userRole = switch (request.getRole()) {
                case OWNER -> User.Role.OWNER;
                case CLIENT -> User.Role.CLIENT;
                case ADMIN -> User.Role.ADMIN;
                case UNRECOGNIZED -> {
                    responseObserver.onError(Status.INVALID_ARGUMENT
                            .withDescription("Invalid role").asRuntimeException());
                    yield null;
                }
            };

            if (userRole == null) return; // Error already sent

            // Create and save User
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .passwordHash(request.getPassword())
                    .phone(request.getPhone())
                    .role(userRole)
                    .build();

            System.out.println("=== SERVER DEBUG: Saving User ===");
            System.out.println("User to save: " + user);

            User savedUser = userRepository.save(user);

            System.out.println("=== SERVER DEBUG: User Saved ===");
            System.out.println("Saved user ID: " + savedUser.getId());

            RegisterUserResponse response = RegisterUserResponse.newBuilder()
                    .setMessage(savedUser.getId().toString())
                    .build();

            System.out.println("=== SERVER DEBUG: Sending Response ===");
            System.out.println("Response: " + response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            System.err.println("=== SERVER DEBUG: Exception ===");
            ex.printStackTrace();
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + ex.getMessage())
                    .withCause(ex)
                    .asRuntimeException());
        }
    }
}