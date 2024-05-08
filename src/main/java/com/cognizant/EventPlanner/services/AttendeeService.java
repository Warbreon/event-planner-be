package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public int countActiveNotifications(String email) {
        return attendeeRepository.countActiveNotifications(email);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public void saveAttendees(Set<Attendee> attendees) {
        attendeeRepository.saveAll(attendees);
    }

    public List<Attendee> findAllByEventId(Long eventId) {
        return attendeeRepository.findAllByEventId(eventId);
    }

    public void removeAttendees(List<Long> attendeesIdsToRemove) {
        attendeeRepository.removeAllByIdIn(attendeesIdsToRemove);
    }

    public void updateEventAttendees(Event event, Set<User> newUsers) {
        List<Attendee> currentEventAttendees = findAllByEventId(event.getId());
        List<Long> toRemove = currentEventAttendees.stream().filter(attendee -> !newUsers.contains(attendee.getUser())).map(Attendee::getId).toList();
        removeAttendees(toRemove);

        Set<Attendee> newAttendees = newUsers.stream()
                .filter(user -> currentEventAttendees.stream().noneMatch(attendee -> attendee.getUser().equals(user)))
                .map(user -> new Attendee(null, RegistrationStatus.ACCEPTED, null,
                        LocalDateTime.now(), null, user, event))
                .collect(Collectors.toSet());

        saveAttendees(newAttendees);
    }

}
