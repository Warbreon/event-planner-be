package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.exception.AttendeeIsAlreadyRegisteredException;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import com.cognizant.EventPlanner.repository.EventRepository;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final AttendeeMapper attendeeMapper;
    private final UserRepository userRepository;

    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        try {
            Optional<User> userOptional = userRepository.findById(request.getUserId());
            User user;
            if (userOptional.isEmpty()) {
                throw new EntityNotFoundException(User.class, request.getUserId());
            }
            user = userOptional.get();

            Optional<Event> eventOptional = eventRepository.findById(request.getEventId());
            Event event;
            if (eventOptional.isEmpty()) {
                throw new EntityNotFoundException(Event.class, request.getEventId());
            }
            event = eventOptional.get();

            checkUserRegistrationStatus(event, request.getUserId());

            Attendee attendeeToRegister = buildAttendee(event, user);
            Attendee attendee = attendeeRepository.save(attendeeToRegister);

            return attendeeMapper.attendeeToDto(attendee);
        } catch (Exception e) {
            return new AttendeeResponseDto(e.getMessage());
        }
    }


    private Attendee buildAttendee(Event event, User user) {
        Attendee attendeeToRegister = Attendee.builder()
                .event(event)
                .isNewNotification(true)
                .user(user)
                .registrationTime(LocalDateTime.now())
                .build();
        if (event.isOpen()) {
            attendeeToRegister.setRegistrationStatus(RegistrationStatus.ACCEPTED);
        } else {
            attendeeToRegister.setRegistrationStatus(RegistrationStatus.PENDING);
            attendeeToRegister.setPaymentStatus(PaymentStatus.PENDING);
        }
        return attendeeToRegister;
    }

    private void checkUserRegistrationStatus(Event event, Long userId) {
        boolean isUserRegistered = eventService.isUserRegistered(event, userId);
        if (isUserRegistered) {
            throw new AttendeeIsAlreadyRegisteredException(userId);
        }
    }
}
