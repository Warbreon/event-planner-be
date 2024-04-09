package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
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
        List<AttendeeResponseDto> attendeesDto = event.getAttendees()
                .stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toList());
        eventDto.setAttendees(attendeesDto);
        eventDto.setCurrentUserRegisteredToEvent(isUserRegistered(event, userId));
        return eventDto;
    }

    public boolean isUserRegistered(Event event, Long userId) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getId().equals(userId));
    }

    public boolean isPaid(Event event) {
        return event.getPrice() != null;
    }
}
