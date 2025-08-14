package com.rentahall.hallservice.service.impl;

import com.rentahall.hallservice.dto.HallRegistrationRequest;
import com.rentahall.hallservice.dto.HallRegistrationResponse;
import com.rentahall.hallservice.entity.Address;
import com.rentahall.hallservice.entity.Hall;
import com.rentahall.hallservice.entity.HallImage;
import com.rentahall.hallservice.repository.AddressRepository;
import com.rentahall.hallservice.repository.HallImageRepository;
import com.rentahall.hallservice.repository.HallRepository;
import com.rentahall.hallservice.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final AddressRepository addressRepository;
    private final HallImageRepository hallImageRepository;

    @Transactional
    public HallRegistrationResponse registerHall(HallRegistrationRequest request) {
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .country(request.getCountry())
                .build();
        addressRepository.save(address);

        Hall hall = Hall.builder()
                .ownerId(request.getOwnerId())
                .address(address)
                .name(request.getName())
                .description(request.getDescription())
                .capacity(request.getCapacity())
                .avgPrice(request.getAvgPrice())
                .build();
        hallRepository.save(hall);

        // Save hall images if provided
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(url -> {
                HallImage image = HallImage.builder()
                        .hall(hall)
                        .imageUrl(url)
                        .build();
                hallImageRepository.save(image);
            });
        }

        return HallRegistrationResponse.builder()
                .hallId(hall.getId())
                .message("Hall registered successfully")
                .build();
    }
}
