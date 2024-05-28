package com.cognizant.EventPlanner.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {

    PASSWORD_RESET("PasswordReset.html"),
    REGISTRATION_ACCEPTED("RegistrationToPrivateEventAccepted.html"),
    REGISTRATION_REJECTED("RegistrationToPrivateEventRejection.html");
    private final String templateName;

}
