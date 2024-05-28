package com.cognizant.EventPlanner.dto.email.private_event_registration;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@SuperBuilder
public class AcceptRegistrationEmailDto extends RegistrationResponseEmailDto {
    private LocalDate eventStartDate;
    private LocalTime eventStartTime;
    private Long eventId;
    private String eventUrl;
    private String eventImageUrl;
}