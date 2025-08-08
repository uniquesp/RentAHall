package com.rentahall.eventservice.service.impl;

import com.rentahall.eventservice.dto.EventRequest;
import com.rentahall.eventservice.dto.EventResponse;
import com.rentahall.eventservice.entity.Event;
import com.rentahall.eventservice.repository.EventRepository;
import com.rentahall.eventservice.service.EventService;
import com.rentahall.eventservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventResponse createEvent(EventRequest request) {
        Event event = Event.builder()
                .name(request.getName().toLowerCase())
                .build();
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    @Override
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        return mapToResponse(event);
    }

    @Override
    public EventResponse getEventByName(String name) {
        Event event = eventRepository.findByName(name.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with name " + name));
        return mapToResponse(event);
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse updateEvent(UUID id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        event.setName(request.getName().toLowerCase());
        Event updated = eventRepository.save(event);
        return mapToResponse(updated);
    }

    @Override
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        eventRepository.delete(event);
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .build();
    }
}