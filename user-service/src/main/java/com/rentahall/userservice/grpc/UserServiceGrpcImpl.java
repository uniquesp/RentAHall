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
            log.info("=== gRPC: Received RegisterUser Request ===");
            log.info("Request details - Name: {}, Email: {}, Phone: {}, Role: {}",
                    request.getName(), request.getEmail(), request.getPhone(), request.getRole());

            // Validate request
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                log.error("Validation failed: Name is required");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Name is required").asRuntimeException());
                return;
            }

            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                log.error("Validation failed: Email is required");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Email is required").asRuntimeException());
                return;
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                log.error("Validation failed: Password is required");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Password is required").asRuntimeException());
                return;
            }

            // Check if user already exists
            if (userService.existsByEmail(request.getEmail())) {
                log.error("User with email {} already exists", request.getEmail());
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
                log.error("Invalid role provided: {}", request.getRole());
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Invalid role").asRuntimeException());
                return;
            }

            // Create User entity
            User user = User.builder()
                    .name(request.getName().trim())
                    .email(request.getEmail().trim().toLowerCase())
                    .passwordHash(request.getPassword())
                    .phone(request.getPhone() != null && !request.getPhone().trim().isEmpty() ?
                            request.getPhone().trim() : null)
                    .role(userRole)
                    .build();

            log.info("=== gRPC: Creating User ===");
            log.info("User entity: {}", user);

            // Save user through service layer
            UserDTO savedUserDTO = userService.createUser(user);

            log.info("=== gRPC: User Created Successfully ===");
            log.info("Saved user ID: {}, Email: {}", savedUserDTO.getId(), savedUserDTO.getEmail());

            // Build response
            RegisterUserResponse response = RegisterUserResponse.newBuilder()
                    .setMessage(savedUserDTO.getId().toString())
                    .build();

            log.info("=== gRPC: Sending Response ===");
            log.info("Response message: {}", response.getMessage());

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException ex) {
            log.error("=== gRPC: Validation Error ===", ex);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            log.error("=== gRPC: Unexpected Error ===", ex);
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
            log.info("=== gRPC: Received ValidateUser Request ===");
            log.info("Validating user with email: {}", request.getEmail());

            // Validate request
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                log.error("Validation failed: Email is required");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Email is required").asRuntimeException());
                return;
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                log.error("Validation failed: Password is required");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Password is required").asRuntimeException());
                return;
            }

            // Validate credentials using service layer
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

                log.info("=== gRPC: User Validation SUCCESS ===");
                log.info("Response - UserId: {}, Role: {}", response.getUserId(), response.getRole());

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                // Return invalid response for security (don't reveal if user exists)
                ValidateUserResponse response = ValidateUserResponse.newBuilder()
                        .setValid(false)
                        .setUserId("")
                        .setRole(Role.CLIENT) // Default role
                        .build();

                log.info("=== gRPC: User Validation FAILED ===");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

        } catch (Exception ex) {
            log.error("=== gRPC: Validation Error ===", ex);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error during validation")
                    .withCause(ex)
                    .asRuntimeException());
        }
    }
}