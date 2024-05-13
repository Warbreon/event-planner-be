package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.BaseEventRegistrationRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public AttendeeResponseDto registerToEvent(BaseEventRegistrationRequestDto request) {
        String userEmail = userDetailsService.getCurrentUserEmail();
        User user = userService.findUserByEmail(userEmail);
        Event event = eventService.findEventById(request.getEventId());

        AttendeeRequestDto attendeeDto = new AttendeeRequestDto();
        attendeeDto.setUserId(user.getId());
        attendeeDto.setEventId(event.getId());

        return registrationService.registerAttendeeToEvent(attendeeDto, user, event);
    }

    public AttendeeResponseDto confirmAttendeeRegistration(Long attendeeId) {
        Attendee confirmedAttendee = attendeeService.confirmAttendeeRegistration(attendeeId);
        return attendeeMapper.attendeeToDto(confirmedAttendee);
    }

    @Transactional
    public void unregisterFromEvent(Long eventId) {
        String userEmail = userDetailsService.getCurrentUserEmail();
        User user = userService.findUserByEmail(userEmail);
        registrationService.unregisterAttendeeFromEvent(user.getId(), eventId);
    }

}
