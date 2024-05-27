package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.facade.EventManagementFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventManagementFacade eventManagementFacade;

    @GetMapping
    public ResponseEntity<?> getEvents(
            @RequestParam(required = false) Set<Long> tagIds,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long excludeEventId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Object events = eventManagementFacade.getEventsFacade(
                Optional.ofNullable(tagIds),
                Optional.ofNullable(days),
                Optional.ofNullable(city),
                Optional.ofNullable(name),
                Optional.ofNullable(excludeEventId),
                Optional.ofNullable(page),
                Optional.ofNullable(size)
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
    public ResponseEntity<EventResponseDto> createNewEvent(@Valid @RequestBody EventRequestDto request) throws IOException {
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

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<EventResponseDto> editEvent(@PathVariable("id") Long id, @Valid @RequestBody EditEventRequestDto request) throws IOException {
        EventResponseDto response  = eventManagementFacade.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @GetMapping("/event/creator")
    public ResponseEntity<Boolean> confirmEventCreator(@RequestParam("userId") Long userId, @RequestParam("eventId") Long eventId) {
        return ResponseEntity.ok(eventManagementFacade.confirmThatEventCreatedByUserExist(eventId, userId));
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<EventResponseDto> cancelEvent(@PathVariable Long id) {
        EventResponseDto response = eventManagementFacade.cancelEvent(id);
        return ResponseEntity.ok(response);
    }
}
