package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AttendeeMapper attendeeMapper;
    private final AttendeeService attendeeService;
    private final EventService eventService;

    public AttendeeResponseDto registerAttendeeToEvent(AttendeeRequestDto request, User user, Event event) {
        Attendee attendeeToRegister = createAttendee(request, user, event);
        Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);

        return attendeeMapper.attendeeToDto(attendee);
    }

    public Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requests, Map<Long, User> userMap, Event event) {
        List<Attendee> attendees = requests.stream()
                .map(request -> createAttendee(request, userMap.get(request.getUserId()), event))
                .collect(Collectors.toList());

        attendees = attendeeService.saveAllAttendees(attendees);

        return attendees.stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toSet());
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
