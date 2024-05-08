package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
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
        AttendeeResponseDto response = attendeeManagementFacade.registerToEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponseDto> getAttendeeNotifications() {
        NotificationResponseDto response = attendeeManagementFacade.getAttendeeNotifications();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{attendeeId}/mark-as-viewed")
    public ResponseEntity<Void> markNotificationAsViewed(@PathVariable Long attendeeId) {
        attendeeManagementFacade.markNotificationAsViewed(attendeeId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/register/{attendeeId}/confirm")
    public ResponseEntity<AttendeeResponseDto> confirmRegistration(@PathVariable Long attendeeId) {
        AttendeeResponseDto response = attendeeManagementFacade.confirmRegistration(attendeeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/register/{attendeeId}/decline")
    public ResponseEntity<AttendeeResponseDto> declineRegistration(@PathVariable Long attendeeId) {
        AttendeeResponseDto response = attendeeManagementFacade.declineRegistration(attendeeId);
        return ResponseEntity.ok(response);
    }
}
