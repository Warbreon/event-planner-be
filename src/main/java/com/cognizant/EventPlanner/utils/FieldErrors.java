package com.cognizant.EventPlanner.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldErrors {
    public static Map<String, String> getFieldErrors(BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        return errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
