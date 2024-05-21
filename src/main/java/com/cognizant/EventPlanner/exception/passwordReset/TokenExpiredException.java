package com.cognizant.EventPlanner.exception.passwordReset;

public class TokenExpiredException extends  RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }

}
