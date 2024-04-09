package com.cognizant.EventPlanner.exception;

public class AttendeeIsAlreadyRegisteredException extends RuntimeException{
    public AttendeeIsAlreadyRegisteredException(Long id) {
        super("Attendee is already registered for the event");
    }
}
