package com.rentahall.featurecatalogservice.repository;

import com.rentahall.featurecatalogservice.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, UUID> {
    Optional<FeatureEntity> findByName(String name);
}
