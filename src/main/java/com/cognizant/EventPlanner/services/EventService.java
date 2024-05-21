package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.EventRepository;
import com.cognizant.EventPlanner.specification.EventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Cacheable(value = "paginatedEvents", key = "{#tagIds.orElse('all'), #days.orElse('all'), #city.orElse('all'), " +
            "#name.orElse('all'), #excludeEventId.orElse('all'), #page, #size, @userDetailsServiceImpl" +
            ".getCurrentUserEmail()}")
    public Page<Event> getPaginatedEvents(Optional<Set<Long>> tagIds, Optional<Integer> days, Optional<String> city,
                                          Optional<String> name, Optional<Long> excludeEventId, Integer page,
                                          Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "eventStart"));
        Specification<Event> spec = buildSpecification(tagIds, days, city, name, excludeEventId);
        return findEventsWithSpec(spec, pageable);
    }

    @Cacheable(value = "events", key = "{#tagIds.orElse('all'), #days.orElse('all'), #city.orElse('all'), #name" +
            ".orElse('all'), #excludeEventId.orElse('all'), @userDetailsServiceImpl.getCurrentUserEmail()}")
    public List<Event> getEventsWithoutPagination(Optional<Set<Long>> tagIds, Optional<Integer> days,
                                                  Optional<String> city, Optional<String> name, Optional<Long> excludeEventId) {
        Specification<Event> spec = buildSpecification(tagIds, days, city, name, excludeEventId);
        return findEventsWithSpec(spec);
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
    }

    public Page<Event> findEventsWithSpec(Specification<Event> spec, Pageable pageable) {
        return eventRepository.findAll(spec, pageable);
    }

    public List<Event> findEventsWithSpec(Specification<Event> spec) {
        return eventRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "eventStart"));
    }

    public List<Event> findEventsByCreator(String email) {
        return eventRepository.findAllByCreatorEmailOrderByEventStartDesc(email);
    }

    public List<Event> findEventsUserIsRegisteredTo(String email) {
        return eventRepository.findAllUserIsRegisteredTo(email);
    }

    public Event findEventByAttendeeId(Long attendeeId) {
        return eventRepository.findEventByAttendeeId(attendeeId);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public boolean isPaid(Event event) {
        return event.getPrice() != null && event.getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public Event prepareEventForCreation(EventRequestDto request, Address address, User user) {
        Event event = eventMapper.dtoToEvent(request);
        event.setCreatedDate(LocalDateTime.now());
        event.setAddress(address);
        event.setCreator(user);
        return event;
    }

    private Specification<Event> buildSpecification(Optional<Set<Long>> tagIds, Optional<Integer> days,
                                                    Optional<String> city, Optional<String> name, Optional<Long> excludeEventId) {
        return Stream.of(
                        tagIds.filter(ids -> !ids.isEmpty()).map(EventSpecifications::hasTags),
                        days.map(EventSpecifications::withinDays),
                        city.map(EventSpecifications::byCity),
                        name.map(EventSpecifications::byName),
                        excludeEventId.map(EventSpecifications::byExcludeEventId)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Specification.where(null), Specification::and);
    }

}
