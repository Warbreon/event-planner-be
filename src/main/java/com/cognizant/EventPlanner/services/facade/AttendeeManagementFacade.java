package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.controller.NotificationController;
import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.NotificationResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeManagementFacade {

    private final UserService userService;
    private final EventService eventService;
    private final RegistrationService registrationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AttendeeService attendeeService;
    private final AttendeeMapper attendeeMapper;
    private final NotificationController notificationController;

    public AttendeeResponseDto registerToEvent(AttendeeRequestDto request) {
        User user = userService.findUserById(request.getUserId());
        Event event = eventService.findEventById(request.getEventId());
        AttendeeResponseDto response =  registrationService.registerAttendeeToEvent(request, user, event);
        notifyEventCreator(event);
        return response;
    }

    public NotificationResponseDto getAttendeeNotifications() {
        String email = userDetailsService.getCurrentUserEmail();
        return attendeeService.getAttendeeNotifications(email);
    }

    public void markNotificationAsViewed(Long attendeeId) {
        attendeeService.markNotificationAsViewed(attendeeId);
        notifyEventCreatorByAttendee(attendeeId);
    }

    public AttendeeResponseDto confirmPendingRegistration(Long attendeeId) {
        Attendee attendee = attendeeService.confirmPendingRegistration(attendeeId);
        notifyEventCreatorByAttendee(attendeeId);
        return attendeeMapper.attendeeToDto(attendee);
    }

    public AttendeeResponseDto declinePendingRegistration(Long attendeeId) {
        Attendee attendee = attendeeService.declinePendingRegistration(attendeeId);
        notifyEventCreatorByAttendee(attendeeId);
        return attendeeMapper.attendeeToDto(attendee);
    }

    private void notifyEventCreatorByAttendee(Long attendeeId) {
        Event event = eventService.findEventByAttendeeId(attendeeId);
        notifyEventCreator(event);
    }

    private void notifyEventCreator(Event event) {
        if (!event.getIsOpen() && event.getCreator() != null) {
            notificationController.notifyEventCreator(event.getCreator().getEmail());
        }
    }

    public void updateEventAttendees(Long eventId, List<Long> userIds) {
        List<Long> userIdsToAdd = new ArrayList<>(userIds);
        List<Attendee> updatedAttendees = new ArrayList<>();
        List<Attendee> eventAttendees = attendeeService.findAllAttendeesByEventId(eventId);
        sortOutAttendees(eventAttendees, userIdsToAdd, updatedAttendees);
        createNewRecordsForAttendees(eventId, userIdsToAdd, updatedAttendees);
        attendeeService.saveAllAttendees(updatedAttendees);
    }

    private void sortOutAttendees(List<Attendee> eventAttendees, List<Long> userIdsToAdd, List<Attendee> updatedAttendees) {
        eventAttendees.forEach(attendee -> {
            Long userId = attendee.getUser().getId();
            if (userIdsToAdd.contains(userId)) {
                attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
                updatedAttendees.add(attendee);
                userIdsToAdd.remove(userId);
            } else {
                attendee.setRegistrationStatus(RegistrationStatus.REJECTED);
                updatedAttendees.add(attendee);
            }
        });
    }

    private void createNewRecordsForAttendees(Long eventId, List<Long> userIdsToAdd, List<Attendee> updatedAttendees) {
        Event event = eventService.findEventById(eventId);
        userIdsToAdd.forEach(userId ->
            {
                User user = userService.findUserById(userId);
                Attendee attendeeToRegister = attendeeMapper.requestDtoToAttendee(new AttendeeRequestDto(userId, eventId), event, user);
                attendeeToRegister.setRegistrationStatus(RegistrationStatus.ACCEPTED);
                updatedAttendees.add(attendeeToRegister);
            }
        );
    }
}
