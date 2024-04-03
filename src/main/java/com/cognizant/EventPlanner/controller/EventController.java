package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

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

}
