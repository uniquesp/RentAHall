package com.rentahall.hallservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressRequest {
    @NotBlank private String street;
    @NotBlank private String city;
    private String state;
    private String pincode;
    private String country = "India";
}
