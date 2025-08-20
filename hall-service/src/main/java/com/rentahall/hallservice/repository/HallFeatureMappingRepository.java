package com.rentahall.hallservice.repository;

import com.rentahall.hallservice.entity.HallFeatureId;
import com.rentahall.hallservice.entity.HallFeatureMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallFeatureMappingRepository extends JpaRepository<HallFeatureMapping, HallFeatureId> {}