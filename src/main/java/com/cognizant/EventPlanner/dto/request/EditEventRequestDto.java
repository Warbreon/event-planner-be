package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditEventRequestDto {

    @Size(max = 255, message = "Event name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 255, message = "Event image URL cannot exceed 255 characters")
    private String imageUrl;

    private Boolean isOpen;

    @Future(message = "Event start date must be in the future")
    private LocalDateTime eventStart;

    @Future(message = "Event end date must be in the future")
    private LocalDateTime eventEnd;

    @Future(message = "Event registration start date must be in the future")
    private LocalDateTime registrationStart;

    @Future(message = "Event registration closure date must be in the future")
    private LocalDateTime registrationEnd;

    private String[] agenda;

    @Min(value = 0, message = "Event price can not be less than 0")
    @Max(value = 10000, message = "Event price can not be greater than 10000")
    private Double price;

    @Min(value = 0, message = "Event tickets count can not be less than 0")
    @Max(value = 10000, message = "Event tickets count can not be greater than 10000")
    private Integer tickets;

    @Size(max = 255, message = "Invite to online meeting cannot exceed 255 characters")
    private String inviteUrl;

    private Set<Long> userIds;

    @Min(value = 0, message = "Address ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Address ID value can not exceed 9223372036854775807")
    private Long addressId;

    private Set<Long> tagIds;
}
