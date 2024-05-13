package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.registration.EventSoldOutException;
import com.cognizant.EventPlanner.exception.registration.RegistrationClosedException;
import com.cognizant.EventPlanner.exception.registration.RegistrationNotOpenException;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EventRegistrationRulesService {

    public void validateEventForRegistration(Event event, User user) {
        if (Objects.equals(user.getEmail(), event.getCreator().getEmail())) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (event.getTickets() == null || event.getTickets() < 1) {
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
