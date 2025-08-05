package com.rentahall.userservice.grpc;


import com.rentahall.userservice.dto.UserDTO;
import com.rentahall.userservice.entity.User;
import com.rentahall.userservice.service.UserService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    @Override
    @Transactional
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        try {
            // Validate request
            request.getName();
            if (request.getName().trim().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Name is required").asRuntimeException());
                return;
            }

            request.getEmail();
            if (request.getEmail().trim().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Email is required").asRuntimeException());
                return;
            }

            request.getPassword();
            if (request.getPassword().trim().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Password is required").asRuntimeException());
                return;
            }

            // Check if user already exists
            if (userService.existsByEmail(request.getEmail())) {
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("User with email already exists").asRuntimeException());
                return;
            }

            // Map gRPC role enum to entity enum
            User.Role userRole;
            try {
                userRole = switch (request.getRole()) {
                    case OWNER -> User.Role.OWNER;
                    case CLIENT -> User.Role.CLIENT;
                    case ADMIN -> User.Role.ADMIN;
                    case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid role");
                };
            } catch (Exception e) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Invalid role").asRuntimeException());
                return;
            }

            // Create User entity
            request.getPhone();
            User user = User.builder()
                    .name(request.getName().trim())
                    .email(request.getEmail().trim().toLowerCase())
                    .passwordHash(request.getPassword())
                    .phone(!request.getPhone().trim().isEmpty() ?
                            request.getPhone().trim() : null)
                    .role(userRole)
                    .build();

            // Save user through service layer
            UserDTO savedUserDTO = userService.createUser(user);

            // Build response
            RegisterUserResponse response = RegisterUserResponse.newBuilder()
                    .setMessage(savedUserDTO.getId().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + ex.getMessage())
                    .withCause(ex)
                    .asRuntimeException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUser(ValidateUserRequest request, StreamObserver<ValidateUserResponse> responseObserver) {
        try {
            // Validate request
            request.getEmail();
            if (request.getEmail().trim().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Email is required").asRuntimeException());
                return;
            }

            request.getPassword();
            if (request.getPassword().trim().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Password is required").asRuntimeException());
                return;
            }

            // Validate credentials using the service layer
            boolean isValid = userService.validateUserCredentials(
                    request.getEmail().trim().toLowerCase(),
                    request.getPassword()
            );

            if (isValid) {
                // Get user details
                UserDTO userDTO = userService.findByEmail(request.getEmail().trim().toLowerCase());

                // Map entity role to gRPC role
                Role grpcRole = switch (User.Role.valueOf(userDTO.getRole())) {
                    case OWNER -> Role.OWNER;
                    case CLIENT -> Role.CLIENT;
                    case ADMIN -> Role.ADMIN;
                };

                ValidateUserResponse response = ValidateUserResponse.newBuilder()
                        .setValid(true)
                        .setUserId(userDTO.getId().toString())
                        .setRole(grpcRole)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                // Return invalid response for security (don't reveal if user exists)
                ValidateUserResponse response = ValidateUserResponse.newBuilder()
                        .setValid(false)
                        .setUserId("")
                        .setRole(Role.CLIENT)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error during validation")
                    .withCause(ex)
                    .asRuntimeException());
        }
    }
}