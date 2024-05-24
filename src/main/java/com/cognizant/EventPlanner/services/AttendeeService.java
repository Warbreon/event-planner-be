package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.AttendeeNotificationResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.NotificationMapper;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final NotificationMapper notificationMapper;

    public NotificationResponseDto getAttendeeNotifications(String email) {
        int activeNotifications = countActiveNotifications(email);
        int totalNotifications = countTotalNotifications(email);
        List<AttendeeNotificationResponseDto> attendees = findPendingAttendeesWithNotifications(email)
                .stream()
                .map(notificationMapper::attendeeNotificationToDto)
                .toList();
        return new NotificationResponseDto(attendees, totalNotifications, activeNotifications);
    }

    @Transactional
    public void markNotificationAsViewed(Long attendeeId) {
        Attendee attendee = findAttendeeById(attendeeId);
        if (attendee.getIsNewNotification()) {
            attendee.setIsNewNotification(false);
            attendeeRepository.save(attendee);
        }
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    @Transactional
    public Attendee confirmPendingRegistration(Long attendeeId) {
        return updateRegistrationStatus(
                attendeeId,
                EnumSet.of(RegistrationStatus.PENDING, RegistrationStatus.REJECTED),
                RegistrationStatus.ACCEPTED
        );
    }

    @Transactional
    public Attendee declinePendingRegistration(Long attendeeId) {
        return updateRegistrationStatus(
                attendeeId,
                EnumSet.of(RegistrationStatus.PENDING, RegistrationStatus.ACCEPTED),
                RegistrationStatus.REJECTED
        );
    }

    private Attendee updateRegistrationStatus(Long attendeeId, EnumSet<RegistrationStatus> allowedStatuses, RegistrationStatus newStatus) {
        Attendee attendee = findAttendeeById(attendeeId);
        if (attendee.getIsNewNotification()) {
            attendee.setIsNewNotification(false);
        }
        if (allowedStatuses.contains(attendee.getRegistrationStatus())) {
            attendee.setRegistrationStatus(newStatus);
            return saveAttendee(attendee);
        }
        return attendee;
    }

    public Attendee findAttendeeById(Long attendeeId) {
        return attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new EntityNotFoundException(Attendee.class, attendeeId));
    }

    public List<Attendee> findPendingAttendeesWithNotifications(String email) {
        return attendeeRepository.findPendingAttendeesWithNotifications(email);
    }

    public int countActiveNotifications(String email) {
        return attendeeRepository.countActiveNotifications(email);
    }

    public int countTotalNotifications(String email) {
        return attendeeRepository.countAllNotifications(email);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
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

    public long countAttendeesByEvent(Long eventId) {
        return attendeeRepository.countByEventId(eventId);
    }

}
