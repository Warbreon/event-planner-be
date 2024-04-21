package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventTagRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.mapper.AttendeeMapper;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.repository.EventRepository;
import jakarta.transaction.Transactional;
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

    private final EntityFinderService entityFinderService;
    private final AttendeeService attendeeService;
    private final TagService tagService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AttendeeMapper attendeeMapper;
    private final TagMapper tagMapper;

    public Set<EventResponseDto> getEvents(Optional<Set<Long>> tagIds, Long userId) {
        List<Event> events = tagIds
                .filter(tagIdsSet -> !tagIdsSet.isEmpty())
                .map(entityFinderService::findEventsByTags)
                .orElseGet(entityFinderService::findAllEvents);
        return events.stream()
                .map(event -> convertEventToDto(event, userId))
                .collect(Collectors.toSet());
    }

    public EventResponseDto getEventById(Long id, Long userId) {
        Event event = entityFinderService.findEventById(id);
        return convertEventToDto(event, userId);
    }

    @Transactional
    public EventResponseDto createNewEvent(EventRequestDto request) {
        Event event = prepareEventForCreation(request);
        event = eventRepository.save(event);
        return buildEventResponse(event, request);
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

    private Event prepareEventForCreation(EventRequestDto request) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(entityFinderService.findAddressById(request.getAddressId()));
        event.setCreator(entityFinderService.findUserById(request.getCreatorId()));
        return event;
    }

    private EventResponseDto buildEventResponse(Event event, EventRequestDto request) {
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        eventResponseDto.setAttendees(registerAttendeesToEvent(request.getAttendees(), eventResponseDto.getId()));
        eventResponseDto.setTags(addTagsToEvent(request.getTags(), eventResponseDto.getId()));
        return eventResponseDto;
    }

    private Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requestSet, Long eventId) {
        return requestSet.stream()
                .peek(item -> item.setEventId(eventId))
                .map(attendeeService::registerToEvent)
                .collect(Collectors.toSet());
    }

    private Set<TagResponseDto> addTagsToEvent(Set<EventTagRequestDto> requestSet, Long eventId) {
        return requestSet.stream()
                .peek(item -> item.setEventId(eventId))
                .map(tagService::addTagToEvent)
                .collect(Collectors.toSet());
    }
}
