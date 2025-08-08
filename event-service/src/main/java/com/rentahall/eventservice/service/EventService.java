package com.rentahall.eventservice.service;

import com.rentahall.eventservice.dto.EventRequest;
import com.rentahall.eventservice.dto.EventResponse;
import com.rentahall.eventservice.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse getEventById(UUID id);
    EventResponse getEventByName(String name);
    List<EventResponse> getAllEvents();
    EventResponse updateEvent(UUID id, EventRequest request);
    void deleteEvent(UUID id);
}
