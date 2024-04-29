package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
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

    public Event prepareEventForCreation(EventRequestDto request, Address address, User user) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(address);
        event.setCreator(user);
        return event;
    }
}
