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

    private AddressRequest address;
    private List<UUID> eventTypeIds;
}
