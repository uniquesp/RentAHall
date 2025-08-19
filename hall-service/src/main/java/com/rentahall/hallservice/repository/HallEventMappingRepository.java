package com.rentahall.hallservice.repository;

import com.rentahall.hallservice.entity.HallEventId;
import com.rentahall.hallservice.entity.HallEventMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HallEventMappingRepository extends JpaRepository<HallEventMapping, HallEventId> {

}
