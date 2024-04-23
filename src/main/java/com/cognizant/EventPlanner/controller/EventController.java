package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.AttendeeService;
import com.cognizant.EventPlanner.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents(@RequestParam Long userId) {
        List<EventResponseDto> events = eventService.getAllEvents(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable(value = "id") Long id, @RequestParam Long userId) {
        EventResponseDto event = eventService.getEventById(id, userId);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/register")
    public ResponseEntity<AttendeeResponseDto> registerToEvent(@Valid @RequestBody AttendeeRequestDto request) {
        AttendeeResponseDto response = attendeeService.registerToEvent(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create/new")
    public ResponseEntity<EventResponseDto> createNewEvent(@Valid @RequestBody EventRequestDto request) {
        return new ResponseEntity<>(eventService.createNewEvent(request), HttpStatus.CREATED);
    }
}
