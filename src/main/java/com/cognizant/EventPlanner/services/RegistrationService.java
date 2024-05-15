package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AttendeeMapper attendeeMapper;
    private final AttendeeService attendeeService;
    private final EventService eventService;

    @Transactional
    public AttendeeResponseDto registerAttendeeToEvent(AttendeeRequestDto request, User user, Event event) {
        Attendee attendeeToRegister = attendeeMapper.requestDtoToAttendee(request, event, user);
        setAttendeeStatuses(attendeeToRegister, event);
        Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);
        return attendeeMapper.attendeeToDto(attendee);
    }

    private void setAttendeeStatuses(Attendee attendee, Event event) {
        if (!event.getIsOpen()) {
            attendee.setIsNewNotification(true);
            attendee.setRegistrationStatus(RegistrationStatus.PENDING);
        }
        if (eventService.isPaid(event)) {
            attendee.setPaymentStatus(PaymentStatus.PENDING);
        }
    }
}
