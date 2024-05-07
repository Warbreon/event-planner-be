package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    @CacheEvict(value = "events", allEntries = true)
    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    public Attendee findAttendeeById(Long id) {
        return attendeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Attendee.class, id));
    }

    public RegistrationStatus getAttendeeRegistrationStatus(Event event, String userEmail) {
        Optional<RegistrationStatus> status = event.getAttendees()
                .stream()
                .filter(attendee -> userEmail.equals(attendee.getUser().getEmail()))
                .map(Attendee::getRegistrationStatus)
                .findFirst();

        return status.orElse(null);
    }

}
