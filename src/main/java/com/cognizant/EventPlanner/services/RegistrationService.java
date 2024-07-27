package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AttendeeMapper attendeeMapper;
    private final AttendeeService attendeeService;
    private final EventService eventService;
    private final UserService userService;
    private final EventRegistrationRulesService eventRegistrationRulesService;

    public AttendeeResponseDto registerAttendeeToEvent(AttendeeRequestDto request, User user, Event event) {
        boolean isUserCreator = isUserCreator(user, event);

        eventRegistrationRulesService.validateEventForRegistration(event, isUserCreator);
        Optional<Attendee> existingAttendee = attendeeService.findAttendeeByUserAndEvent(user.getId(), event.getId());

        if (existingAttendee.isPresent()) {
            return attendeeMapper.attendeeToDto(existingAttendee.get());
        }

        Attendee attendeeToRegister = createAttendee(request, user, event, isUserCreator);
        Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);

        return attendeeMapper.attendeeToDto(attendee);
    }

    public Set<AttendeeResponseDto> registerAttendeesToEvent(Set<Long> userIds, Event event) {
        List<Long> userIdsToAdd = new ArrayList<>(userIds);
        List<Attendee> updatedAttendees = new ArrayList<>();
        List<Attendee> eventAttendees = attendeeService.findAllAttendeesByEventId(event.getId());
        sortOutAttendees(eventAttendees, userIdsToAdd, updatedAttendees, event);
        createNewRecordsForAttendees(event.getId(), userIdsToAdd, updatedAttendees, event);

        List<Attendee> savedAttendees = attendeeService.saveAllAttendees(updatedAttendees);

        return savedAttendees.stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toSet());
    }

    public void unregisterAttendeeFromEvent(Long userId, Long eventId) {
        Attendee attendee = attendeeService.findAttendeeByUserAndEvent(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("No registration found for provided user and event " +
                        "IDs"));

        attendeeService.deleteAttendee(attendee);
    }

    public Attendee createAttendee(AttendeeRequestDto request, User user, Event event, boolean isUserCreator) {
        Attendee attendee = attendeeMapper.requestDtoToAttendee(request, event, user);
        setAttendeeStatuses(attendee, event, isUserCreator);
        return attendee;
    }

    private void setAttendeeStatuses(Attendee attendee, Event event, boolean isUserCreator) {
        if (isUserCreator || (event.getIsOpen() && !eventService.isPaid(event))) {
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
        } else if (!event.getIsOpen()) {
            attendee.setIsNewNotification(true);
            attendee.setRegistrationStatus(RegistrationStatus.PENDING);
        }
    }

    public void updateAttendeeStatuses(boolean isUserCreator, Attendee attendee, Event event, RegistrationStatus registrationStatus, PaymentStatus paymentStatus) {
        attendee.setRegistrationStatus(registrationStatus);
        attendee.setPaymentStatus(paymentStatus);
        if (!event.getIsOpen() && !isUserCreator) {
            attendee.setIsNewNotification(true);
        }
        if (isUserCreator) {
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
            attendee.setPaymentStatus(PaymentStatus.PAID);
        }
    }

    private boolean isUserCreator(User user, Event event) {
        return Objects.equals(user.getEmail(), event.getCreator().getEmail());
    }

    private void sortOutAttendees(List<Attendee> eventAttendees, List<Long> userIdsToAdd, List<Attendee> updatedAttendees, Event event) {
        eventAttendees.forEach(attendee -> {
            Long userId = attendee.getUser().getId();
            if (userIdsToAdd.contains(userId) || isUserCreator(attendee.getUser(), event)) {
                attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
                updatedAttendees.add(attendee);
                userIdsToAdd.remove(userId);
            } else {
                attendee.setRegistrationStatus(RegistrationStatus.REJECTED);
                updatedAttendees.add(attendee);
            }
        });
    }

    private void createNewRecordsForAttendees(Long eventId, List<Long> userIdsToAdd, List<Attendee> updatedAttendees, Event event) {
        userIdsToAdd.forEach(userId ->
                {
                    User user = userService.findUserById(userId);
                    Attendee attendeeToRegister = attendeeMapper.requestDtoToAttendee(new AttendeeRequestDto(userId, eventId), event, user);
                    attendeeToRegister.setRegistrationStatus(RegistrationStatus.ACCEPTED);
                    if (eventService.isPaid(event)) {
                        attendeeToRegister.setPaymentStatus(PaymentStatus.PENDING);
                    }
                    updatedAttendees.add(attendeeToRegister);
                }
        );
    }

}
