package com.rentahall.hallservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallRegistrationRequest {
    private UUID ownerId;
    private String name;
    private String description;
    private Integer capacity;
    private BigDecimal avgPrice;

    // Address
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private List<String> imageUrls;

    // Features & Event Types
    private List<UUID> featureIds;
    private List<UUID> eventTypeIds;
}
