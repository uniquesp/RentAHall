package com.rentahall.hallservice.controller;

import com.rentahall.hallservice.dto.HallRegistrationRequest;
import com.rentahall.hallservice.dto.HallRegistrationResponse;
import com.rentahall.hallservice.service.HallService;
import com.rentahall.hallservice.service.impl.HallServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService service;

    @PostMapping
    public ResponseEntity<HallRegistrationResponse> registerHall(@RequestBody HallRegistrationRequest request) {
        HallRegistrationResponse response = service.createHall(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<HallRegistrationResponse> getAllHalls() {
        return service.getAllHalls();
    }

}
