package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.services.facade.AttendeeManagementFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendee")
public class AttendeeController {

    private final AttendeeManagementFacade attendeeManagementFacade;

    @PostMapping("/register")
    public ResponseEntity<AttendeeResponseDto> registerToEvent(@Valid @RequestBody AttendeeRequestDto request) {
        AttendeeResponseDto response = attendeeManagementFacade.registerAttendee(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{attendeeId}")
    public ResponseEntity<AttendeeResponseDto> confirmRegistration(@PathVariable Long attendeeId) {
        AttendeeResponseDto response = attendeeManagementFacade.confirmAttendeeRegistration(attendeeId);
        return ResponseEntity.ok(response);
    }

}
