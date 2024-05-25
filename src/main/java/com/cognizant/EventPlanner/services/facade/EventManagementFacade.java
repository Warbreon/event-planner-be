package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.mapper.EventMapper;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
    private final ImageUploadService imageUploadService;
    private final AttendeeService attendeeService;

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

    public boolean confirmThatEventCreatedByUserExist(Long eventId, Long creatorId) {
        return eventService.findThatEventByIdAndCreatorIdExists(eventId, creatorId);
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

    @Transactional
    public EventResponseDto updateEvent(Long id, EditEventRequestDto requestDto) throws IOException {
        return convertEventToDto(eventService.saveEvent(setUpdatedEventValues(id, requestDto)));
    }


    private Event setUpdatedEventValues(Long id, EditEventRequestDto requestDto) throws IOException {
        Event newEventValues = eventMapper.editEventRequestDtoToEvent(requestDto);
        Event eventToEdit = eventService.findEventById(id);

        BeanWrapper eventWrapper = new BeanWrapperImpl(eventToEdit);
        BeanWrapper newValuesWrapper = new BeanWrapperImpl(newEventValues);

        for (PropertyDescriptor propertyDescriptor : newValuesWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            if (eventWrapper.isWritableProperty(propertyName)
                    && newValuesWrapper.getPropertyValue(propertyName) != null
                    && !Objects.equals(eventWrapper.getPropertyValue(propertyName), newValuesWrapper.getPropertyValue(propertyName))) {
                eventWrapper.setPropertyValue(propertyName, newValuesWrapper.getPropertyValue(propertyName));
            }
        }

        eventService.handleEventDatesUpdate(requestDto, eventToEdit);


        if (requestDto.getAddressId() != null) {
            addressService.updateEventAddress(eventToEdit, requestDto.getAddressId());
        }

        if (requestDto.getAttendeeIds() != null) {
            updateEventAttendeesFacade(eventToEdit, requestDto.getAttendeeIds());
        }

        if (requestDto.getTagIds() != null) {
            tagService.updateEventTags(eventToEdit, requestDto.getTagIds());
        }

        if (requestDto.getImageBase64() != null) {
            String imageUrl = imageUploadService.uploadImageToAzure(requestDto.getImageBase64());
            eventToEdit.setImageUrl(imageUrl);
        }

        if (requestDto.getCardImageBase64() != null) {
            String cardImageUrl = imageUploadService.uploadImageToAzure(requestDto.getCardImageBase64());
            eventToEdit.setCardImageUrl(cardImageUrl);
        }

        return eventToEdit;
    }

    private void updateEventAttendeesFacade(Event event, Set<Long> newUserIds) {
        Set<User> newUsers = newUserIds.stream()
                .map(userService::findUserById)
                .collect(Collectors.toSet());
        attendeeService.updateEventAttendees(event, newUsers);
    }

    private EventResponseDto buildEventResponse(Event event, EventRequestDto request) {
        EventResponseDto eventResponseDto = eventMapper.eventToDto(event);
        Set<AttendeeRequestDto> attendeeRequestDtos = getAttendeeRequestDtos(event.getId(), request.getAttendeeIds());
        eventResponseDto.setAttendees(registerAttendeesToEvent(attendeeRequestDtos, event));
        eventResponseDto.setTags(tagService.addTagsToEvent(request.getTagIds(), event));
        return eventResponseDto;
    }

    public Set<AttendeeResponseDto> registerAttendeesToEvent(Set<AttendeeRequestDto> requests, Event event) {
        Set<Long> userIds = requests.stream()
                .map(AttendeeRequestDto::getUserId)
                .collect(Collectors.toSet());

        List<User> users = userService.findUsersByIds(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<Long> registeredUserIds = attendeeService.findAttendeesByUsersAndEvent(userIds, event.getId())
                .stream()
                .map(Attendee::getUser)
                .map(User::getId)
                .collect(Collectors.toSet());

        return registrationService.registerAttendeesToEvent(requests, userMap, registeredUserIds, event);
    }

    private EventResponseDto convertEventToDto(Event event) {
        EventResponseDto eventDto = eventMapper.eventToDto(event);
        eventDto.setTags(tagService.mapEventTags(event.getTags()));
        eventDto.setCurrentUserRegistrationStatus(attendeeService.getAttendeeRegistrationStatus(
                event,
                userDetailsService.getCurrentUserEmail()
        ));
        return eventDto;
    }

    private Set<AttendeeRequestDto> getAttendeeRequestDtos(Long eventId, Set<Long> attendeeIds) {
        return attendeeIds.stream().map(attendeeId -> new AttendeeRequestDto(attendeeId, eventId)).collect(Collectors.toSet());
    }

}
