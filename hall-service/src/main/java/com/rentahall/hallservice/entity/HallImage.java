package com.rentahall.hallservice.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "hall_images")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallImage {

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;
}
