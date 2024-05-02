package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.facade.EventManagementFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventManagementFacade eventManagementFacade;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(
            @RequestParam(required = false) Set<Long> tagIds,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String city
    ) {
        List<EventResponseDto> events = eventManagementFacade.getEvents(
                Optional.ofNullable(tagIds),
                Optional.ofNullable(days),
                Optional.ofNullable(city)
        );
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable(value = "id") Long id) {
        EventResponseDto event = eventManagementFacade.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @PostMapping("/create/new")
    public ResponseEntity<EventResponseDto> createNewEvent(@Valid @RequestBody EventRequestDto request) {
        EventResponseDto response = eventManagementFacade.createNewEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @GetMapping("/created-by-user")
    public ResponseEntity<List<EventResponseDto>> getEventsCreatedByUser() {
        List<EventResponseDto> response = eventManagementFacade.getEventsCreatedByUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-registered")
    public ResponseEntity<List<EventResponseDto>> getEventsUserIsRegisteredTo() {
        List<EventResponseDto> response = eventManagementFacade.getEventsUserIsRegisteredTo();
        return ResponseEntity.ok(response);
    }
}
