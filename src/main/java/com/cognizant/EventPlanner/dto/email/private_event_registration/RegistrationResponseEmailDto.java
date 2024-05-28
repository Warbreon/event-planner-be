package com.cognizant.EventPlanner.dto.email.private_event_registration;

import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class RegistrationResponseEmailDto extends BaseEmailDetailsDto {
    private String eventCreatorFirstName;
    private String eventCreatorLastName;
    private String eventCreatorEmail;
    private String eventName;
}
