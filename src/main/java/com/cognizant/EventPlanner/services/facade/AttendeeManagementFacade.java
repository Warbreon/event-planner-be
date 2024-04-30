package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.EventService;
import com.cognizant.EventPlanner.services.RegistrationService;
import com.cognizant.EventPlanner.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendeeManagementFacade {

    private final UserService userService;
    private final EventService eventService;
    private final RegistrationService registrationService;

    @Transactional
    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        User user = userService.findUserById(request.getUserId());
        Event event = eventService.findEventById(request.getEventId());
        return registrationService.registerAttendeeToEvent(request, user, event);
    }
}
