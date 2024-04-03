package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.exception.EventNotFoundException;
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

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return convertEventToDto(event);
    }

    private EventResponseDto convertEventToDto(Event event) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        List<AttendeeResponseDto> attendeesDto = event.getAttendees().stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toList());
        eventDto.setAttendees(attendeesDto);
        return eventDto;
    }
}
