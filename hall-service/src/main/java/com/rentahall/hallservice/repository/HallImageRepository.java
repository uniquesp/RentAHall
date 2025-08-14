package com.rentahall.hallservice.repository;

import com.rentahall.hallservice.entity.HallImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HallImageRepository extends JpaRepository<HallImage, UUID> {}
