package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AttendeeMapper attendeeMapper;
    private final AttendeeService attendeeService;
    private final EventService eventService;

    public AttendeeResponseDto registerAttendeeToEvent(AttendeeRequestDto request, User user, Event event) {
        Optional<Attendee> existingAttendee = attendeeService.findAttendeeByUserAndEvent(user.getId(), event.getId());

        if (existingAttendee.isPresent()) {
            return attendeeMapper.attendeeToDto(existingAttendee.get());
        }

        Attendee attendeeToRegister = createAttendee(request, user, event);
        Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);

        return attendeeMapper.attendeeToDto(attendee);
    }

    public Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requests, Map<Long, User> userMap, Set<Long> registeredUserIds, Event event) {
        List<Attendee> attendeesToSave = requests.stream()
                .filter(request -> !registeredUserIds.contains(request.getUserId()))
                .map(request -> createAttendee(request, userMap.get(request.getUserId()), event))
                .collect(Collectors.toList());

        List<Attendee> savedAttendees = attendeeService.saveAllAttendees(attendeesToSave);

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

    private Attendee createAttendee(AttendeeRequestDto request, User user, Event event) {
        Attendee attendee = attendeeMapper.requestDtoToAttendee(request, event, user);
        setAttendeeStatuses(attendee, event);
        return attendee;
    }

    private void setAttendeeStatuses(Attendee attendee, Event event) {
        if (event.getIsOpen()) {
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
        } else {
            attendee.setIsNewNotification(true);
            attendee.setRegistrationStatus(RegistrationStatus.PENDING);
        }

        if (eventService.isPaid(event)) {
            attendee.setPaymentStatus(PaymentStatus.PENDING);
        }
    }

}
