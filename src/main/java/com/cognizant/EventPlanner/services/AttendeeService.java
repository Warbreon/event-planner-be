package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

}
