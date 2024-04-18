package com.cognizant.EventPlanner.validation.dateRangeValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startDate;
    private String endDate;
    private String message;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        Object startDateValue = beanWrapper.getPropertyValue(startDate);
        Object endDateValue = beanWrapper.getPropertyValue(endDate);

        if (startDateValue == null || endDateValue == null) {
            return false;
        }

        LocalDateTime startDate = (LocalDateTime) startDateValue;
        LocalDateTime endDate = (LocalDateTime) endDateValue;

        if (endDate.isBefore(startDate)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(this.message)
                    .addPropertyNode(this.endDate)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}