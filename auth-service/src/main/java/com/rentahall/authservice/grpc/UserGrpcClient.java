package com.rentahall.authservice.grpc;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public RegisterUserResponse registerUser(String name, String email, String hashedPassword, String phone, Role role) {
        try {
            // Build request with debugging
            RegisterUserRequest.Builder requestBuilder = RegisterUserRequest.newBuilder();

            if (name != null) requestBuilder.setName(name);
            if (email != null) requestBuilder.setEmail(email);
            if (hashedPassword != null) requestBuilder.setPassword(hashedPassword);
            if (phone != null) requestBuilder.setPhone(phone);

            requestBuilder.setRole(Objects.requireNonNullElse(role, Role.CLIENT));

            RegisterUserRequest request = requestBuilder.build();


            // Test connection first
            RegisterUserResponse response = stub.registerUser(request);

            return response;

        } catch (StatusRuntimeException ex) {
            if (ex.getTrailers() != null) {
                System.err.println("Trailers: " + ex.getTrailers());
            } else {
                System.err.println("Trailers: NULL - This might be your issue!");
            }
            throw new RuntimeException("gRPC call failed: " + ex.getStatus(), ex);

        } catch (Exception ex) {
            System.err.println("=== DEBUG: Unexpected Exception ===");
            System.err.println("Exception type: " + ex.getClass().getSimpleName());
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to register user: " + ex.getMessage(), ex);
        }
    }

    public ValidateUserResponse validateUser(String email, String plainPassword) {
        try {
            // Build validation request - send plain password to
            // User Service will handle bcrypt comparison with stored hash
            ValidateUserRequest request = ValidateUserRequest.newBuilder()
                    .setEmail(email)
                    .setPassword(plainPassword) // Send plain password
                    .build();

            // Call gRPC service - User Service will compare plain password with stored hash
            ValidateUserResponse response = stub.validateUser(request);
            return response;

        } catch (StatusRuntimeException ex) {
            System.err.println("=== DEBUG: gRPC Status Exception ===");
            System.err.println("Status: " + ex.getStatus());
            System.err.println("Description: " + ex.getStatus().getDescription());
            if (ex.getTrailers() != null) {
                System.err.println("Trailers: " + ex.getTrailers());
            } else {
                System.err.println("Trailers: NULL");
            }
            throw new RuntimeException("gRPC validation call failed: " + ex.getStatus(), ex);

        } catch (Exception ex) {
            System.err.println("=== DEBUG: Unexpected Exception in validateUser ===");
            System.err.println("Exception type: " + ex.getClass().getSimpleName());
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to validate user: " + ex.getMessage(), ex);
        }
    }
}
