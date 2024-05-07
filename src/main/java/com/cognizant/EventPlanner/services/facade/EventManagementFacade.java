package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private EventResponseDto buildEventResponse(Event event, EventRequestDto request) {
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        eventResponseDto.setAttendees(registerAttendeesToEvent(request.getAttendees(), event));
        eventResponseDto.setTags(tagService.addTagsToEvent(request.getTagIds(), event));
        return eventResponseDto;
    }

    private Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requestSet, Event event) {
        return requestSet.stream()
                .map(request -> registrationService.registerAttendeeToEvent(
                        request,
                        userService.findUserById(request.getUserId()),
                        event
                ))
                .collect(Collectors.toSet());
    }

    private EventResponseDto convertEventToDto(Event event) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        eventDto.setTags(tagService.mapEventTags(event.getTags()));
        eventDto.setCurrentUserRegistrationStatus(attendeeService.getAttendeeRegistrationStatus(
                event,
                userDetailsService.getCurrentUser().getUsername()
        ));
        return eventDto;
    }
}
