package com.rentahall.hallservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallRegistrationResponse {
    private UUID id;
    private UUID ownerId;
    private String name;
    private String description;
    private Integer capacity;
    private BigDecimal avgPrice;

    private List<UUID> eventIds;
    private List<UUID> featureIds;
}