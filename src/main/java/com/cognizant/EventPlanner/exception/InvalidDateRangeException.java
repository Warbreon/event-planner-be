package com.cognizant.EventPlanner.exception;

public class InvalidDateRangeException extends IllegalArgumentException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}