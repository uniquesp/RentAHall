package com.rentahall.hallservice.controller;

import com.rentahall.eventtype.grpc.EventTypeList;
import com.rentahall.hallservice.dto.EventResponse;
import com.rentahall.hallservice.service.impl.HallServiceTry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/halls")
public class HallController {

    private final HallServiceTry hallService;

    public HallController(HallServiceTry hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/event-types")
    public List<EventResponse> getEventTypes() {
        return hallService.getAllEventTypes().getEventTypesList().stream()
                .map(et -> new EventResponse(UUID.fromString(et.getId()), et.getName()))
                .toList();
    }
}
