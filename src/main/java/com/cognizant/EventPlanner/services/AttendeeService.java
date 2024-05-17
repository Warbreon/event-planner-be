package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.AttendeeNotificationResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.NotificationMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

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
        attendee.setIsNewNotification(false);
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
}
