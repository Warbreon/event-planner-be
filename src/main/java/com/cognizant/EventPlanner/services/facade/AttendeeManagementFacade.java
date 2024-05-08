package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendeeManagementFacade {

    private final UserService userService;
    private final EventService eventService;
    private final RegistrationService registrationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AttendeeService attendeeService;
    private final AttendeeMapper attendeeMapper;

    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        User user = userService.findUserById(request.getUserId());
        Event event = eventService.findEventById(request.getEventId());
        return registrationService.registerAttendeeToEvent(request, user, event);
    }

    public NotificationResponseDto getAttendeeNotifications() {
        String email = userDetailsService.getCurrentUserEmail();
        return attendeeService.getAttendeeNotifications(email);
    }

    public void markNotificationAsViewed(Long attendeeId) {
        attendeeService.markNotificationAsViewed(attendeeId);
    }

    public AttendeeResponseDto confirmPendingRegistration(Long attendeeId) {
        Attendee attendee = attendeeService.confirmPendingRegistration(attendeeId);
        return attendeeMapper.attendeeToDto(attendee);
    }

    public AttendeeResponseDto declinePendingRegistration(Long attendeeId) {
        Attendee attendee = attendeeService.declinePendingRegistration(attendeeId);
        return attendeeMapper.attendeeToDto(attendee);
    }
}
