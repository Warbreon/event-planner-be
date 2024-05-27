package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.exception.registration.EventSoldOutException;
import com.cognizant.EventPlanner.exception.registration.RegistrationClosedException;
import com.cognizant.EventPlanner.exception.registration.RegistrationNotOpenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class EventRegistrationExceptionControllerAdvice {

    @ExceptionHandler(value = {EventSoldOutException.class, RegistrationNotOpenException.class,
            RegistrationClosedException.class})
    public ResponseEntity<Object> handleRegistrationException(RuntimeException ex) {
        log.error("Registration exception", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
