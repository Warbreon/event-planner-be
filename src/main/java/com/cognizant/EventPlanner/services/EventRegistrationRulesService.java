package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.registration.EventSoldOutException;
import com.cognizant.EventPlanner.exception.registration.RegistrationClosedException;
import com.cognizant.EventPlanner.exception.registration.RegistrationNotOpenException;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventRegistrationRulesService {

    private final AttendeeService attendeeService;

    public void validateEventForRegistration(Event event, boolean isUserCreator) {
        if (isUserCreator) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        long registeredAttendees = attendeeService.countAcceptedAttendeesByEvent(event.getId());

        if (event.getTickets() == null || event.getTickets() <= registeredAttendees ) {
            throw new EventSoldOutException();
        }

        if (event.getRegistrationStart() != null && now.isBefore(event.getRegistrationStart())) {
            throw new RegistrationNotOpenException(event.getRegistrationStart());
        }

        if (event.getRegistrationEnd() != null && now.isAfter(event.getRegistrationEnd())) {
            throw new RegistrationClosedException();
        }
    }

}
