package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final EntityFinderService entityFinderService;
    private final AttendeeRepository attendeeRepository;
    private final AttendeeMapper attendeeMapper;

    @Transactional
    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        User user = entityFinderService.findUserById(request.getUserId());
        Event event = entityFinderService.findEventById(request.getEventId());

        Attendee attendeeToRegister = attendeeMapper.requestDtoToAttendee(request, event, user);
        setAttendeeStatuses(attendeeToRegister, event);
        Attendee attendee = attendeeRepository.save(attendeeToRegister);
        return attendeeMapper.attendeeToDto(attendee);
    }

    public boolean isEventPaid(Event event) {
        return event.getPrice() != null && event.getPrice() > 0;
    }

    private void setAttendeeStatuses(Attendee attendee, Event event) {
        if (!event.getIsOpen()) {
            attendee.setIsNewNotification(true);
            attendee.setRegistrationStatus(RegistrationStatus.PENDING);
        }
        if (isEventPaid(event)) {
            attendee.setPaymentStatus(PaymentStatus.PENDING);
        }
    }
}
