package com.rentahall.hallservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hall_event_type_mappings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class HallEventMapping {

    @EmbeddedId
    private HallEventId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hallId")
    @JoinColumn(name = "hall_id")
    private Hall hall;
}
