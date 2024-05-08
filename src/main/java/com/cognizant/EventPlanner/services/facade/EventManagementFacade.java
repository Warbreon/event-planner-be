package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;


import java.beans.PropertyDescriptor;
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

    public Object getEventsFacade(Optional<Set<Long>> tagIds, Optional<Integer> days, Optional<String> city, Optional<String> name, Optional<Integer> page, Optional<Integer> size) {
        return (page.isPresent() && size.isPresent())
                ? eventService.getPaginatedEvents(tagIds, days, city, name, page.get(), size.get()).map(this::convertEventToDto)
                : eventService.getEventsWithoutPagination(tagIds, days, city, name).stream().map(this::convertEventToDto).toList();
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
    public EventResponseDto updateEvent(Long id, EditEventRequestDto requestDto) {
        Event newEventValues = eventMapper.editEventRequestDtoToEvent(requestDto);
        Event eventToEdit = eventService.findEventById(id);

        BeanWrapper eventWrapper = new BeanWrapperImpl(eventToEdit);
        BeanWrapper newValuesWrapper = new BeanWrapperImpl(newEventValues);

        for (PropertyDescriptor propertyDescriptor : newValuesWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            if (eventWrapper.isWritableProperty(propertyName) && newValuesWrapper.getPropertyValue(propertyName) != null) {
                eventWrapper.setPropertyValue(propertyName, newValuesWrapper.getPropertyValue(propertyName));
            }
        }

        if (requestDto.getAddressId() != null) {
            addressService.updateEventAddress(eventToEdit, requestDto.getAddressId());
        }

        if (requestDto.getUserIds() != null) {
            updateEventAttendeesFacade(eventToEdit, requestDto.getUserIds());
        }

        if (requestDto.getTagIds() != null) {
            tagService.updateEventTags(eventToEdit, requestDto.getTagIds());
        }

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
