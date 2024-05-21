package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.exception.passwordReset.PasswordReuseException;
import com.cognizant.EventPlanner.exception.passwordReset.TokenExpiredException;
import com.cognizant.EventPlanner.exception.passwordReset.TokenNotFoundException;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
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
public class PasswordResetControllerAdvice {

    @ExceptionHandler(PasswordReuseException.class)
    public ResponseEntity<?> handlePasswordReuseException(PasswordReuseException ex, HttpServletRequest request) {
        log.error("Password reuse attempted: {}", ex.getMessage());
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request) {
        log.error("Expired token accessed: {}", ex.getMessage());
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.GONE, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException ex, HttpServletRequest request) {
        log.error("Token not found: {}", ex.getMessage());
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

}
