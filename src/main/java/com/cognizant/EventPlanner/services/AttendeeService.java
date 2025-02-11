package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.AttendeeNotificationResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.NotificationMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AttendeeRepository;
import com.cognizant.EventPlanner.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.EnumSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final NotificationMapper notificationMapper;
    private final TransactionRepository transactionRepository;

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
        Attendee attendee = updateRegistrationStatus(
                attendeeId,
                EnumSet.of(RegistrationStatus.PENDING, RegistrationStatus.REJECTED),
                RegistrationStatus.ACCEPTED
        );
        attendee = updatePaymentStatus(
                attendee.getEvent(),
                attendeeId,
                EnumSet.of(PaymentStatus.PRE_REFUND),
                PaymentStatus.PAID
        );
        return attendee;
    }

    @Transactional
    public Attendee declinePendingRegistration(Long attendeeId) {
        Attendee attendee = updateRegistrationStatus(
                attendeeId,
                EnumSet.of(RegistrationStatus.PENDING, RegistrationStatus.ACCEPTED),
                RegistrationStatus.REJECTED
        );
        attendee = updatePaymentStatus(
                attendee.getEvent(),
                attendeeId,
                EnumSet.of(PaymentStatus.PAID),
                PaymentStatus.PRE_REFUND
        );
        return attendee;
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

    private Attendee updatePaymentStatus(Event event, Long attendeeId, EnumSet<PaymentStatus> allowedStatuses, PaymentStatus newStatus) {
        Attendee attendee = findAttendeeById(attendeeId);
        if (event.getPrice().compareTo(BigDecimal.ZERO) != 0) {
            if (allowedStatuses.contains(attendee.getPaymentStatus())) {
                attendee.setPaymentStatus(newStatus);
                return saveAttendee(attendee);
            }
        }
        return attendee;
    }
  
    public List<Attendee> findAllAttendeesByEventId(Long eventId){
        return attendeeRepository.findAttendeesByEventId(eventId);
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

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public void saveAttendees(Set<Attendee> attendees) {
        attendeeRepository.saveAll(attendees);
    }

    public List<Attendee> findAllByEventId(Long eventId) {
        return attendeeRepository.findAllByEventId(eventId);
    }

    public void removeAttendees(List<Long> attendeesIdsToRemove) {
        transactionRepository.removeAllByIdIn(attendeesIdsToRemove);
        attendeeRepository.removeAllByIdIn(attendeesIdsToRemove);
    }

    public void updateEventAttendees(Event event, Set<User> newUsers) {
        List<Attendee> currentEventAttendees = findAllByEventId(event.getId());
        List<Long> toRemove = currentEventAttendees.stream().filter(attendee -> !newUsers.contains(attendee.getUser())).map(Attendee::getId).toList();
        removeAttendees(toRemove);

        Set<Attendee> newAttendees = newUsers.stream()
                .filter(user -> currentEventAttendees.stream().noneMatch(attendee -> attendee.getUser().equals(user)))
                .map(user -> new Attendee(null, RegistrationStatus.ACCEPTED, null,
                        LocalDateTime.now(), null, user, event, null))
                .collect(Collectors.toSet());

        saveAttendees(newAttendees);
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

    public PaymentStatus getAttendeePaymentStatus(Event event, String userEmail) {
        return attendeeRepository.findAttendeePaymentStatus(event.getId(), userEmail)
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

    public long countAcceptedAttendeesByEvent(Long eventId) {
        return attendeeRepository.countAcceptedAttendeesByEventId(eventId);
    }

}
