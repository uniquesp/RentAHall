package com.rentahall.hallservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "hall_feature_mappings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class HallFeatureMapping {

    @EmbeddedId
    private HallFeatureId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hallId")
    @JoinColumn(name = "hall_id")
    private Hall hall;
}