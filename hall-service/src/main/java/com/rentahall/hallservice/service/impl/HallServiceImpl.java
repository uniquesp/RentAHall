package com.rentahall.hallservice.service.impl;

import com.rentahall.hallservice.dto.HallRegistrationRequest;
import com.rentahall.hallservice.dto.HallRegistrationResponse;
import com.rentahall.hallservice.entity.*;
import com.rentahall.hallservice.repository.AddressRepository;
import com.rentahall.hallservice.repository.HallEventMappingRepository;
import com.rentahall.hallservice.repository.HallRepository;
import com.rentahall.hallservice.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final AddressRepository addressRepository;
    private final HallRepository hallRepository;
    private final HallEventMappingRepository hallEventMappingRepository;

    @Transactional
    @Override
    public HallRegistrationResponse createHall(HallRegistrationRequest request) {
        // 1️ Save Address first
        Address address = Address.builder()
                .street(request.getAddress().getStreet())
                .city(request.getAddress().getCity())
                .state(request.getAddress().getState())
                .pincode(request.getAddress().getPincode())
                .country(request.getAddress().getCountry())
                .build();
        address = addressRepository.save(address);

        // 2️ Save Hall with reference to Address
        Hall hall = Hall.builder()
                .ownerId(request.getOwnerId())
                .name(request.getName())
                .description(request.getDescription())
                .capacity(request.getCapacity())
                .avgPrice(request.getAvgPrice())
                .address(address)
                .build();
        final Hall savedHall = hallRepository.save(hall);

        // 3. Save mappings
        List<HallEventMapping> mappings = request.getEventTypeIds().stream()
                .map(eventId -> HallEventMapping.builder()
                        .id(new HallEventId(savedHall.getId(), eventId))
                        .hall(savedHall)
                        .build())
                .toList();

        hallEventMappingRepository.saveAll(mappings);

        // 4. Build response
        return HallRegistrationResponse.builder()
                .id(hall.getId())
                .ownerId(hall.getOwnerId())
                .name(hall.getName())
                .description(hall.getDescription())
                .capacity(hall.getCapacity())
                .avgPrice(hall.getAvgPrice())
                .eventIds(request.getEventTypeIds())  // here also fix name
                .build();
    }

    @Override
    public List<HallRegistrationResponse> getAllHalls() {
        List<Hall> halls = hallRepository.findAll();

        return halls.stream().map(hall -> {
            // fetch related event IDs from mapping table
            List<UUID> eventIds = hallEventMappingRepository.findAll().stream()
                    .filter(mapping -> mapping.getId().getHallId().equals(hall.getId()))
                    .map(mapping -> mapping.getId().getEventTypeId())
                    .toList();

            return HallRegistrationResponse.builder()
                    .id(hall.getId())
                    .ownerId(hall.getOwnerId())
                    .name(hall.getName())
                    .description(hall.getDescription())
                    .capacity(hall.getCapacity())
                    .avgPrice(hall.getAvgPrice())
                    .eventIds(eventIds)
                    .build();
        }).toList();
    }
}




