package com.rentahall.authservice.grpc;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public RegisterUserResponse registerUser(String name, String email, String hashedPassword, String phone, Role role) {

        // Debug: Print all input values
        System.out.println("=== DEBUG: Input Parameters ===");
        System.out.println("Name: " + (name != null ? "'" + name + "'" : "NULL"));
        System.out.println("Email: " + (email != null ? "'" + email + "'" : "NULL"));
        System.out.println("HashedPassword: " + (hashedPassword != null ? "'" + hashedPassword.substring(0, 10) + "...'" : "NULL"));
        System.out.println("Phone: " + (phone != null ? "'" + phone + "'" : "NULL"));
        System.out.println("Role: " + (role != null ? role : "NULL"));

        try {
            // Build request with debugging
            RegisterUserRequest.Builder requestBuilder = RegisterUserRequest.newBuilder();

            if (name != null) requestBuilder.setName(name);
            if (email != null) requestBuilder.setEmail(email);
            if (hashedPassword != null) requestBuilder.setPassword(hashedPassword);
            if (phone != null) requestBuilder.setPhone(phone);
            if (role != null) {
                requestBuilder.setRole(role);
            } else {
                requestBuilder.setRole(Role.CLIENT);
            }

            RegisterUserRequest request = requestBuilder.build();

            // Debug: Print the built request
            System.out.println("=== DEBUG: Built Request ===");
            System.out.println("Request: " + request);
            System.out.println("Request role: " + request.getRole());
            System.out.println("Request serialized size: " + request.getSerializedSize());

            // Test connection first
            System.out.println("=== DEBUG: Making gRPC Call ===");
            RegisterUserResponse response = stub.registerUser(request);

            System.out.println("=== DEBUG: Received Response ===");
            System.out.println("Response: " + response);

            return response;

        } catch (StatusRuntimeException ex) {
            System.err.println("=== DEBUG: gRPC Status Exception ===");
            System.err.println("Status: " + ex.getStatus());
            System.err.println("Code: " + ex.getStatus().getCode());
            System.err.println("Description: " + ex.getStatus().getDescription());
            System.err.println("Cause: " + ex.getCause());

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
}
