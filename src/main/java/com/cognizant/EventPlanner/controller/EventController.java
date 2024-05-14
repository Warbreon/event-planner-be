package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.services.ImageUploadService;
import com.cognizant.EventPlanner.services.facade.EventManagementFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventManagementFacade eventManagementFacade;
    private final ImageUploadService imageUploadService;

    @GetMapping
    public ResponseEntity<?> getEvents(
            @RequestParam(required = false) Set<Long> tagIds,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Object events = eventManagementFacade.getEventsFacade(
                Optional.ofNullable(tagIds),
                Optional.ofNullable(days),
                Optional.ofNullable(city),
                Optional.ofNullable(name),
                Optional.ofNullable(page),
                Optional.ofNullable(size)
        );
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable(value = "id") Long id) {
        EventResponseDto event = eventManagementFacade.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @PostMapping("/create/new")
    public ResponseEntity<EventResponseDto> createNewEvent(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("isOpen") boolean isOpen,
            @RequestParam("eventStart") String eventStart,
            @RequestParam("eventEnd") String eventEnd,
            @RequestParam("registrationStart") String registrationStart,
            @RequestParam("registrationEnd") String registrationEnd,
            @RequestParam("price") Double price,
            @RequestParam("creatorId") Long creatorId,
            @RequestParam("addressId") Long addressId,
            @RequestParam("attendees") String attendeesJson,
            @RequestParam("tagIds") String tagIdsJson
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Set<AttendeeRequestDto> attendees = new HashSet<>(Arrays.asList(mapper.readValue(attendeesJson, AttendeeRequestDto[].class)));
        Set<Long> tagIds = new HashSet<>(Arrays.asList(mapper.readValue(tagIdsJson, Long[].class)));

        EventRequestDto request = new EventRequestDto();
        request.setName(name);
        request.setDescription(description);
        request.setImage(image);
        request.setIsOpen(isOpen);
        request.setEventStart(LocalDateTime.parse(eventStart));
        request.setEventEnd(LocalDateTime.parse(eventEnd));
        request.setRegistrationStart(LocalDateTime.parse(registrationStart));
        request.setRegistrationEnd(LocalDateTime.parse(registrationEnd));
        request.setPrice(price);
        request.setCreatorId(creatorId);
        request.setAddressId(addressId);
        request.setAttendees(attendees);
        request.setTagIds(tagIds);

        EventResponseDto response = eventManagementFacade.createNewEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = imageUploadService.uploadImageToAzure(image);
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @GetMapping("/created-by-user")
    public ResponseEntity<List<EventResponseDto>> getEventsCreatedByUser() {
        List<EventResponseDto> response = eventManagementFacade.getEventsCreatedByUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-registered")
    public ResponseEntity<List<EventResponseDto>> getEventsUserIsRegisteredTo() {
        List<EventResponseDto> response = eventManagementFacade.getEventsUserIsRegisteredTo();
        return ResponseEntity.ok(response);
    }
}
