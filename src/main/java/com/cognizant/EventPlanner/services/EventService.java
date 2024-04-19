package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final TagMapper tagMapper;

    public Set<EventResponseDto> getEvents(Optional<Set<Long>> tagIds, Long userId) {
        List<Event> events = tagIds
                .filter(tagIdsSet -> !tagIdsSet.isEmpty())
                .map(this::findEventsByTags)
                .orElseGet(this::findAllEvents);
        return events.stream()
                .map(event -> convertEventToDto(event, userId))
                .collect(Collectors.toSet());
    }

    public EventResponseDto getEventById(Long id, Long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
        return convertEventToDto(event, userId);
    }

    private List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    private List<Event> findEventsByTags(Set<Long> tagIds) {
        return eventRepository.findByTags(tagIds, tagIds.size());
    }

    private EventResponseDto convertEventToDto(Event event, Long userId) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        eventDto.setAttendees(mapEventAttendees(event.getAttendees()));
        eventDto.setTags(mapEventTags(event.getTags()));
        eventDto.setCurrentUserRegisteredToEvent(isUserRegistered(event, userId));
        return eventDto;
    }

    private Set<AttendeeResponseDto> mapEventAttendees(Set<Attendee> attendees) {
        return attendees.stream()
                .map(attendeeMapper::attendeeToDto)
                .collect(Collectors.toSet());
    }

    private Set<TagResponseDto> mapEventTags(Set<EventTag> eventTags) {
        return eventTags.stream()
                .map(EventTag::getTag)
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }

    private boolean isUserRegistered(Event event, Long userId) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getId().equals(userId));
    }

    public EventResponseDto createNewEvent(EventRequestDto request) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(addressService.getAddressById(request.getAddressId()));
        event.setCreator(userService.getUserById(request.getCreatorId()));
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
