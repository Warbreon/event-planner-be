package com.cognizant.EventPlanner.services;

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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
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

    public boolean isPaid(Event event) {
        return event.getPrice() != null && event.getPrice() > 0;
    }
}
