package com.rentahall.hallservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class HallFeatureId implements Serializable {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "hall_id", columnDefinition = "VARCHAR(36)")
    private UUID hallId;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "feature_id", columnDefinition = "VARCHAR(36)")
    private UUID featureId;
}
