package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import com.cognizant.EventPlanner.specification.EventSpecifications;
import com.cognizant.EventPlanner.util.FieldUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventManagementFacade {

    private final EventService eventService;
    private final TagService tagService;
    private final EventMapper eventMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final AddressService addressService;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final AttendeeService attendeeService;

    @Cacheable(value = "events", key = "{#tagIds.orElse('all'), #days.orElse('all'), #city.orElse('all'), #name.orElse('all'), @userDetailsServiceImpl.getCurrentUserEmail()}")
    public List<EventResponseDto> getEvents(
            Optional<Set<Long>> tagIds,
            Optional<Integer> days,
            Optional<String> city,
            Optional<String> name
    ) {
        Specification<Event> spec = Specification.where(null);

        if (tagIds.isPresent() && !tagIds.get().isEmpty()) {
            spec = spec.and(EventSpecifications.hasTags(tagIds.get()));
        }
        if (days.isPresent()) {
            spec = spec.and(EventSpecifications.withinDays(days.get()));
        }
        if (city.isPresent()) {
            spec = spec.and(EventSpecifications.byCity(city.get()));
        }
        if (name.isPresent()) {
            spec = spec.and(EventSpecifications.byName(name.get()));
        }

        return eventService.findEventsWithSpec(spec)
                .stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventService.findEventById(id);
        return convertEventToDto(event);
    }

    @Transactional
    public EventResponseDto createNewEvent(EventRequestDto request) {
        Address address = addressService.findAddressById(request.getAddressId());
        User user = userService.findUserById(request.getCreatorId());
        Event event = eventService.prepareEventForCreation(request, address, user);
        event = eventService.saveEvent(event);
        return buildEventResponse(event, request);
    }

    public List<EventResponseDto> getEventsCreatedByUser() {
        String email = userDetailsService.getCurrentUserEmail();
        return eventService.findEventsByCreator(email)
                .stream()
                .map(this::convertEventToDto)
                .toList();
    }

    public List<EventResponseDto> getEventsUserIsRegisteredTo() {
        String email = userDetailsService.getCurrentUserEmail();
        return eventService.findEventsUserIsRegisteredTo(email)
                .stream()
                .map(this::convertEventToDto)
                .toList();
    }

    @Transactional
    @Modifying
    public EventResponseDto updateEvent(Long id, EditEventRequestDto requestDto) {
        Event eventToEdit = eventService.findEventById(id);

        FieldUtils.updateField(eventToEdit::setName, requestDto.getName());
        FieldUtils.updateField(eventToEdit::setDescription, requestDto.getDescription());
        FieldUtils.updateField(eventToEdit::setImageUrl, requestDto.getImageUrl());
        FieldUtils.updateField(eventToEdit::setIsOpen, requestDto.getIsOpen());
        FieldUtils.updateField(eventToEdit::setEventStart, requestDto.getEventStart());
        FieldUtils.updateField(eventToEdit::setEventEnd, requestDto.getEventEnd());
        FieldUtils.updateField(eventToEdit::setRegistrationStart, requestDto.getRegistrationStart());
        FieldUtils.updateField(eventToEdit::setRegistrationEnd, requestDto.getRegistrationEnd());
        FieldUtils.updateField(eventToEdit::setAgenda, requestDto.getAgenda());
        FieldUtils.updateField(eventToEdit::setTickets, requestDto.getTickets());
        FieldUtils.updateField(eventToEdit::setPrice, requestDto.getPrice());
        FieldUtils.updateField(eventToEdit::setInviteUrl, requestDto.getInviteUrl());
        FieldUtils.updateField(addressId -> addressService.updateEventAddress(eventToEdit, addressId), requestDto.getAddressId());
        FieldUtils.updateField(userIds -> updateEventAttendeesFacade(eventToEdit, userIds), requestDto.getUserIds());
        FieldUtils.updateField(eventTagIds -> tagService.updateEventTags(eventToEdit, eventTagIds), requestDto.getTagIds());

        Event updatedEvent = eventService.updateEvent(eventToEdit);
        return convertEventToDto(updatedEvent);
    }

    private void updateEventAttendeesFacade(Event event, Set<Long> newUserIds) {
        Set<User> newUsers = newUserIds.stream()
                .map(userService::findUserById)
                .collect(Collectors.toSet());
        attendeeService.updateEventAttendees(event, newUsers);
    }

    private EventResponseDto buildEventResponse(Event event, EventRequestDto request) {
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        eventResponseDto.setAttendees(registerAttendeesToEvent(request.getAttendees(), event));
        eventResponseDto.setTags(tagService.addTagsToEvent(request.getTagIds(), event));
        return eventResponseDto;
    }

    private Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requestSet, Event event) {
        return requestSet.stream()
                .map(request -> registrationService.registerAttendeeToEvent(request, userService.findUserById(request.getUserId()), event))
                .collect(Collectors.toSet());
    }

    private EventResponseDto convertEventToDto(Event event) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        eventDto.setTags(tagService.mapEventTags(event.getTags()));
        eventDto.setCurrentUserRegisteredToEvent(userService.isUserRegistered(event, userDetailsService.getCurrentUserEmail()));
        return eventDto;
    }
}
