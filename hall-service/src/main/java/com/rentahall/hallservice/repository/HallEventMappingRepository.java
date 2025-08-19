package com.rentahall.hallservice.repository;

import com.rentahall.hallservice.entity.HallEventId;
import com.rentahall.hallservice.entity.HallEventMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HallEventMappingRepository extends JpaRepository<HallEventMapping, HallEventId> {

    @Query("select m.id.eventTypeId from HallEventMapping m where m.id.hallId = :hallId")
    List<UUID> findEventTypeIdsByHallId(UUID hallId);

}
