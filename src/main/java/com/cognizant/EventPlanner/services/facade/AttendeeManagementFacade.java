package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.BaseEventRegistrationRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
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
