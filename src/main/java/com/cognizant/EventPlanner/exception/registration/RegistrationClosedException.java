package com.cognizant.EventPlanner.exception.registration;

public class RegistrationClosedException extends RuntimeException {

    public RegistrationClosedException() {
        super("Registration has ended.");
    }

}
