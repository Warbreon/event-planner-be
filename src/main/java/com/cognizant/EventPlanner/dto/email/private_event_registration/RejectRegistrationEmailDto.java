package com.cognizant.EventPlanner.dto.email.private_event_registration;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RejectRegistrationEmailDto extends RegistrationResponseEmailDto {
    private String eventPlannerUrl;
}