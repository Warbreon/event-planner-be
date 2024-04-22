package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.dto.response.ErrorResponse;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestExceptionControllerAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Malformed JSON request", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.BAD_REQUEST,
                "Malformed JSON request.", request);
    }
}
