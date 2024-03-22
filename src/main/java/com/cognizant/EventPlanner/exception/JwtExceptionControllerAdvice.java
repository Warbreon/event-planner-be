package com.cognizant.EventPlanner.exception;

import com.cognizant.EventPlanner.dto.response.ErrorResponse;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtExceptionControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(JwtExceptionControllerAdvice.class);

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException ex, HttpServletRequest request) {
        log.error("Unsupported JWT token", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Unsupported JWT token.", request);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex, HttpServletRequest request) {
        log.error("Malformed JWT token", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Malformed JWT token.", request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        log.error("Expired JWT token", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.UNAUTHORIZED, "JWT token has expired, please log in again.", request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        log.error("Invalid JWT token", ex);
        return ErrorResponseEntityUtil.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Invalid JWT token.", request);
    }

}
