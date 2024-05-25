package com.cognizant.EventPlanner.exception.registration;

public class EventSoldOutException extends RuntimeException {

    public EventSoldOutException() {
        super("Tickets are sold out.");
    }

}
