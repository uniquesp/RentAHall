package com.rentahall.hallservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallRegistrationResponse {
    private UUID hallId;
    private String message;
}
