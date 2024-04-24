package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.dto.response.ErrorResponse;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ValidationExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors()
                                                            .stream()
                                                            .map(ObjectError::getDefaultMessage)
                                                            .collect(Collectors.toList());
        String errorMessage = String.join(";", errorMessages);
        log.error("Validation error: {}", errorMessage);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, request);
    }
}
