package com.rentahall.hallservice.controller;

import com.rentahall.hallservice.dto.HallRegistrationRequest;
import com.rentahall.hallservice.dto.HallRegistrationResponse;
import com.rentahall.hallservice.service.HallService;
import com.rentahall.hallservice.service.impl.HallServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService service;

//    @PostMapping
//    public ResponseEntity<HallRegistrationResponse> registerHall(@RequestBody HallRegistrationRequest request) {
//        HallRegistrationResponse response = service.createHall(request);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HallRegistrationResponse> createHall(
            @RequestPart("data") HallRegistrationRequest hallRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(service.createHall(hallRequest, images));
    }



    @GetMapping
    public List<HallRegistrationResponse> getAllHalls() {
        return service.getAllHalls();
    }

}
