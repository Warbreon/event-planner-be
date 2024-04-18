package com.cognizant.EventPlanner.validation.dateRangeValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DateRangeContainer.class)
public @interface DateRange {
    String message() default "End date can't be before start date";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};

    String startDate();
    String endDate();
}