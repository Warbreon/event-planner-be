package com.cognizant.EventPlanner.exception.passwordReset;

public class PasswordReuseException extends RuntimeException {

    public PasswordReuseException() {
        super("New password cannot be the same as the current password.");
    }

}