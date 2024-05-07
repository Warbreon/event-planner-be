package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.AttendeeService;
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
    private final AttendeeService attendeeService;
    private final AttendeeMapper attendeeMapper;

    @Transactional
    public AttendeeResponseDto registerAttendee(AttendeeRequestDto request) {
        User user = userService.findUserByEmail(request.getUserEmail());
        Event event = eventService.findEventById(request.getEventId());
        return registrationService.registerAttendee(request, user, event);
    }

    @Transactional
    public AttendeeResponseDto confirmAttendeeRegistration(Long attendeeId) {
        Attendee attendee = attendeeService.findAttendeeById(attendeeId);

        if (attendee.getRegistrationStatus() == RegistrationStatus.PENDING) {
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
            attendeeService.saveAttendee(attendee);
        }

        return attendeeMapper.attendeeToDto(attendee);
    }

}
