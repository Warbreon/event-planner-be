package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    public List<Attendee> findAllByEventId(Long eventId) {
        return attendeeRepository.findAllByEventId(eventId);
    }

    @Transactional
    @Modifying
    public void removeAttendee(Long id) {
        attendeeRepository.removeById(id);
    }

    @Transactional
    public void updateEventAttendees(Event event, Set<User> newUsers) {
        List<Attendee> currentEventAttendees = findAllByEventId(event.getId());
        Set<Long> newUserIds = newUsers.stream().map(User::getId).collect(Collectors.toSet());

        currentEventAttendees.forEach(attendee -> {
            if (!newUserIds.contains(attendee.getUser().getId())) {
                removeAttendee(attendee.getId());
            }
        });

        Set<User> currentUsers = currentEventAttendees.stream()
                .map(Attendee::getUser)
                .collect(Collectors.toSet());
        newUsers.stream()
                .filter(user -> !currentUsers.contains(user))
                .forEach(user -> saveAttendee(new Attendee(null, RegistrationStatus.ACCEPTED, null,
                        LocalDateTime.now(), false, user, event)));
    }

}
