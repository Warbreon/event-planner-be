package com.cognizant.EventPlanner.dto;
import com.cognizant.EventPlanner.validation.dateRangeValidation.DateRange;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DateRange(startDate = "eventStart", endDate = "eventEnd", message = "Event end date can not be before event start date")
@DateRange(startDate = "registrationStart", endDate = "registrationEnd", message = "Registration end date can not be before registration start date")
@DateRange(startDate = "registrationEnd", endDate = "eventStart", message = "Event can't start before registration to the event wasn't closed")
public abstract class EventDto {

    @NotBlank(message = "Event must have a name")
    @Size(max = 255, message = "Event name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Event must have a description")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Event must have an image")
    @Size(max = 255, message = "Event image URL cannot exceed 255 characters")
    private String imageUrl;

    @NotNull(message = "Event must be open or private")
    private Boolean isOpen;

    @Future(message = "Event start date must be in the future")
    private LocalDateTime eventStart;

    @Future(message = "Event end date must be in the future")
    private LocalDateTime eventEnd;

    private LocalDateTime registrationStart;

    @Future(message = "Event registration closure date must be in the future")
    private LocalDateTime registrationEnd;

    private String[] agenda;

    @Min(value = 0, message = "Event price can not be less than 0")
    @Max(value = 10000, message = "Event price can not be greater than 10000")
    private Double price;

    @Size(max = 255, message = "Invite to online meeting cannot exceed 255 characters")
    private String inviteUrl;
}
