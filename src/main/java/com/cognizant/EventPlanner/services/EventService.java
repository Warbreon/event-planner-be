package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.repository.AddressRepository;
import com.cognizant.EventPlanner.repository.EventRepository;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AttendeeService attendeeService;
    private final AddressRepository addressRepository;
    private final EventMapper eventMapper;
    private final AttendeeMapper attendeeMapper;

    public List<EventResponseDto> getAllEvents(Long userId) {
        return eventRepository.findAll()
                .stream()
                .map(event -> convertEventToDto(event, userId))
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id, Long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
        return convertEventToDto(event, userId);
    }

    private EventResponseDto convertEventToDto(Event event, Long userId) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        Set<AttendeeResponseDto> attendeesDto = event.getAttendees()
                .stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toSet());
        eventDto.setAttendees(attendeesDto);
        eventDto.setCurrentUserRegisteredToEvent(isUserRegistered(event, userId));
        return eventDto;
    }

    private boolean isUserRegistered(Event event, Long userId) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getId().equals(userId));
    }

    public EventResponseDto createNewEvent(EventRequestDto request) {
        Event event = eventMapper.dtoToEvent(request);

        event.setCreatedDate(LocalDateTime.now());
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException(Address.class, request.getAddressId()));
        event.setAddress(address);
        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException(User.class, request.getCreatorId()));
        event.setCreator(user);
        event = eventRepository.save(event);

        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        Set<AttendeeResponseDto> attendeeDtoList = new HashSet<>();

        for (AttendeeRequestDto attendeeFromRequest : request.getAttendees()) {
            attendeeFromRequest.setEventId(eventResponseDto.getId());
            AttendeeResponseDto attendeeDto = attendeeService.registerToEvent(attendeeFromRequest);
            attendeeDtoList.add(attendeeDto);
        }

       eventResponseDto.setAttendees(attendeeDtoList);
       return eventResponseDto;
    }
}
