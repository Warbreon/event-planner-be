package com.cognizant.EventPlanner.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "Password must be at least 8 characters long and include an uppercase letter, a number, " +
            "and a special character.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
