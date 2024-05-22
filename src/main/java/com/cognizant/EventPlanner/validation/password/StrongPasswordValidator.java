package com.cognizant.EventPlanner.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.Predicate;

import static com.cognizant.EventPlanner.constants.ValidationConstants.*;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final Predicate<String> HAS_MINIMUM_LENGTH = password -> password.length() >= MIN_LENGTH;
    private static final Predicate<String> HAS_UPPERCASE_LETTER = password -> password.matches(REGEX_UPPERCASE);
    private static final Predicate<String> HAS_DIGIT = password -> password.matches(REGEX_DIGIT);
    private static final Predicate<String> HAS_SPECIAL_CHARACTER = password -> password.matches(REGEX_SPECIAL_CHAR);

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        return HAS_MINIMUM_LENGTH.test(password)
                && HAS_UPPERCASE_LETTER.test(password)
                && HAS_DIGIT.test(password)
                && HAS_SPECIAL_CHARACTER.test(password);
    }

}
