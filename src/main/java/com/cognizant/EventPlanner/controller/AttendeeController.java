package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.BaseEventRegistrationRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.services.facade.AttendeeManagementFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

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
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'EVENT_ADMIN')")
    @PatchMapping("/{eventId}/updateAttendees")
    public ResponseEntity<Void> updateEventAttendees(@PathVariable Long eventId, @Valid @RequestBody Set<Long> userIds){
        attendeeManagementFacade.updateEventAttendees(eventId,userIds);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<List<AttendeeResponseDto>> getEventAttendees(@PathVariable Long eventId){
        List<AttendeeResponseDto> response = attendeeManagementFacade.getEventAttendees(eventId);
        return ResponseEntity.ok(response);
    }
}
