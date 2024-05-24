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

import java.io.IOException;
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
    private final ImageUploadService imageUploadService;

    public Object getEventsFacade(
            Optional<Set<Long>> tagIds,
            Optional<Integer> days,
            Optional<String> city,
            Optional<String> name,
            Optional<Long> excludeEventId,
            Optional<Integer> page,
            Optional<Integer> size
    ) {
        if (page.isPresent() && size.isPresent()) {
            return eventService.getPaginatedEvents(
                tagIds,
                days,
                city,
                name,
                excludeEventId,
                page.get(),
                size.get()
            ).map(this::convertEventToDto);
        } else {
            return eventService.getEventsWithoutPagination(
                tagIds,
                days,
                city,
                name,
                excludeEventId
            ).stream()
            .map(this::convertEventToDto)
            .toList();
        }
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventService.findEventById(id);
        return convertEventToDto(event);
    }

    @Transactional
    public EventResponseDto createNewEvent(EventRequestDto request) throws IOException {
        String imageUrl = imageUploadService.uploadImageToAzure(request.getImageBase64());
        String cardImageUrl = imageUploadService.uploadImageToAzure(request.getCardImageBase64());
        Address address = addressService.findAddressById(request.getAddressId());
        String userEmail = userDetailsService.getCurrentUserEmail();
        User user = userService.findUserByEmail(userEmail);
        Event event = eventService.prepareEventForCreation(request, address, user);
        event.setImageUrl(imageUrl);
        event.setCardImageUrl(cardImageUrl);
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

    public EventResponseDto cancelEvent(Long id) {
        Event event = eventService.cancelEvent(id);
        return convertEventToDto(event);
    }

    private EventResponseDto buildEventResponse(Event event, EventRequestDto request) {
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        Set<AttendeeRequestDto> attendeeRequestDtos = getAttendeeRequestDtos(event.getId(), request.getAttendeeIds());
        eventResponseDto.setAttendees(registerAttendeesToEvent(attendeeRequestDtos, event));
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

    private Set<AttendeeRequestDto> getAttendeeRequestDtos(Long eventId, Set<Long> attendeeIds) {
        return attendeeIds.stream().map(attendeeId -> new AttendeeRequestDto(attendeeId, eventId)).collect(Collectors.toSet());
    }

}
