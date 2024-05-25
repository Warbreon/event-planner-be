package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.BaseEventRegistrationRequestDto;
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
    public ResponseEntity<AttendeeResponseDto> registerToEvent(@Valid @RequestBody BaseEventRegistrationRequestDto request) {
        AttendeeResponseDto response = attendeeManagementFacade.registerToEvent(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/unregister/{eventId}")
    public ResponseEntity<Void> unregisterFromEvent(@PathVariable Long eventId) {
        attendeeManagementFacade.unregisterFromEvent(eventId);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<AttendeeResponseDto> confirmPendingRegistration(@PathVariable Long attendeeId) {
        AttendeeResponseDto response = attendeeManagementFacade.confirmPendingRegistration(attendeeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/register/{attendeeId}/decline")
    public ResponseEntity<AttendeeResponseDto> declinePendingRegistration(@PathVariable Long attendeeId) {
        AttendeeResponseDto response = attendeeManagementFacade.declinePendingRegistration(attendeeId);
        return ResponseEntity.ok(response);
    }
}
