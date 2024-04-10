package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class AttendeeController {

    private final AttendeeService attendeeService;

    @PostMapping("/register")
    public ResponseEntity<AttendeeResponseDto> registerToEvent(@RequestBody AttendeeRequestDto request) {
        AttendeeResponseDto response = attendeeService.registerToEvent(request);
        return ResponseEntity.ok(response);
    }
}
