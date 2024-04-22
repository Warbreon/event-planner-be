package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.dto.response.ErrorResponse;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SecurityExceptionControllerAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.FORBIDDEN,
                "You do not have permission to access this resource.", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication error: {}", ex.getMessage());
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.UNAUTHORIZED,
                "Authentication failed. Please check your credentials.", request);
    }
}
