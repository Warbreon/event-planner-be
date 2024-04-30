package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final AttendeeService attendeeService;
    private final AddressService addressService;
    private final EventMapper eventMapper;
    private final AttendeeMapper attendeeMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
        return convertEventToDto(event);
    }

    private EventResponseDto convertEventToDto(Event event) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        Set<AttendeeResponseDto> attendeesDto = event.getAttendees()
                .stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toSet());
        eventDto.setAttendees(attendeesDto);
        eventDto.setCurrentUserRegisteredToEvent(isUserRegistered(event,
                userDetailsService.getCurrentUser().getUsername()));
        return eventDto;
    }

    private boolean isUserRegistered(Event event, String userEmail) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getEmail().equals(userEmail));
    }

    public EventResponseDto createNewEvent(EventRequestDto request) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(addressService.getAddressById(request.getAddressId()));
        event.setCreator(userService.findUserById(request.getCreatorId()));
        event = eventRepository.save(event);
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        eventResponseDto.setAttendees(registerAttendeesToEvent(request.getAttendees(), eventResponseDto.getId()));
        return eventResponseDto;
    }

    private Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requestSet, Long EventId) {
        return requestSet.stream().peek(item -> item.setEventId(EventId))
                .map(attendeeService::registerToEvent).collect(Collectors.toSet());
    }
}
