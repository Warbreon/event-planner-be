package com.cognizant.EventPlanner.dto.email;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseEmailDetailsDto {

    private final EmailType emailType;
    private final String recipientEmail;
    private final String name;

}
