package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.User;
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
    private final TagMapper tagMapper;
    private final EventMapper eventMapper;

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
    }

    public List<Event> findEventsByTags(Set<Long> tagIds) {
        return eventRepository.findByTags(tagIds, tagIds.size());
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public boolean isPaid(Event event) {
        return event.getPrice() != null && event.getPrice() > 0;
    }

    public Set<TagResponseDto> mapEventTags(Set<EventTag> eventTags) {
        return eventTags.stream()
                .map(EventTag::getTag)
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }

    public boolean isUserRegistered(Event event, String userEmail) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getEmail().equals(userEmail));
    }

    public Event prepareEventForCreation(EventRequestDto request, Address address, User user) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(address);
        event.setCreator(user);
        return event;
    }

    public EventResponseDto convertToDto(Event event, String userEmail) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        eventDto.setTags(mapEventTags(event.getTags()));
        eventDto.setCurrentUserRegisteredToEvent(isUserRegistered(event, userEmail));
        return eventDto;
    }
}
