package com.cognizant.EventPlanner.exception.registration;

import java.time.LocalDateTime;

public class RegistrationNotOpenException extends RuntimeException {

    public RegistrationNotOpenException(LocalDateTime startDate) {
        super("Registration starts on " + startDate.toString());
    }

}
