package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Set<EventResponseDto>> getEvents(@RequestParam(required = false) Set<Long> tagIds) {
        Set<EventResponseDto> events = eventService.getEvents(Optional.ofNullable(tagIds));
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable(value = "id") Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @PostMapping("/create/new")
    public ResponseEntity<EventResponseDto> createNewEvent(@Valid @RequestBody EventRequestDto request) {
        EventResponseDto response = eventService.createNewEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
