package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Attendee findAttendeeById(Long id) {
        return attendeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Attendee.class, id));
    }

    public Optional<Attendee> findAttendeeByUserAndEvent(Long userId, Long eventId) {
        return attendeeRepository.findByUserIdAndEventId(userId, eventId);
    }

    public List<Attendee> findAttendeesByUsersAndEvent(Set<Long> userIds, Long eventId) {
        return attendeeRepository.findAllByUserIdsAndEventId(userIds, eventId);
    }

    public RegistrationStatus getAttendeeRegistrationStatus(Event event, String userEmail) {
        return attendeeRepository.findAttendeeRegistrationStatus(event.getId(), userEmail)
                .orElse(null);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public List<Attendee> saveAllAttendees(Iterable<Attendee> attendees) {
        return attendeeRepository.saveAll(attendees);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public void deleteAttendee(Attendee attendee) {
        attendeeRepository.delete(attendee);
    }

    @Transactional
    public Attendee confirmAttendeeRegistration(Long attendeeId) {
        Attendee attendee = findAttendeeById(attendeeId);

        if (attendee.getRegistrationStatus() == RegistrationStatus.PENDING) {
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
            saveAttendee(attendee);
        }

        return attendee;
    }

    public long countAttendeesByEvent(Long eventId) {
        return attendeeRepository.countByEventId(eventId);
    }

}
