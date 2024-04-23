package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.dto.response.ErrorResponse;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order
@Slf4j
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.", request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("Entity not found", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

}
