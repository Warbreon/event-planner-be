package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import com.cognizant.EventPlanner.repository.EventRepository;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final AttendeeMapper attendeeMapper;
    private final UserRepository userRepository;

    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(User.class, request.getUserId()));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EntityNotFoundException(Event.class, request.getEventId()));

        Attendee attendeeToRegister = buildAttendee(event, user);
        Attendee attendee = attendeeRepository.save(attendeeToRegister);
        return attendeeMapper.attendeeToDto(attendee);
    }

    public boolean isEventPaid(Event event) {
        return event.getPrice() != null && event.getPrice() > 0;
    }

    private Attendee buildAttendee(Event event, User user) {
        Attendee attendeeToRegister = Attendee.builder()
                .event(event)
                .user(user)
                .registrationTime(LocalDateTime.now())
                .build();
        if (!event.getIsOpen()) {
            attendeeToRegister.setIsNewNotification(true);
            attendeeToRegister.setRegistrationStatus(RegistrationStatus.PENDING);
        } else {
            attendeeToRegister.setRegistrationStatus(RegistrationStatus.ACCEPTED);
        }

        if (isEventPaid(event)) {
            attendeeToRegister.setPaymentStatus(PaymentStatus.PENDING);
        }
        return attendeeToRegister;
    }
}
