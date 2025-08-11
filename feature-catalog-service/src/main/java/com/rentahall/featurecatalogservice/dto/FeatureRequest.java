package com.rentahall.featurecatalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureRequest {

    @NotBlank(message = "Event name is required")
    private String name;
}
