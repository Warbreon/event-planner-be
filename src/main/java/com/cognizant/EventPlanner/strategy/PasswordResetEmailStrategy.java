package com.cognizant.EventPlanner.strategy;

import com.cognizant.EventPlanner.constants.EmailType;
import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PasswordResetEmailStrategy implements EmailStrategy {

    @Override
    public EmailType getEmailType() {
        return EmailType.PASSWORD_RESET;
    }

    @Override
    public String getSubject() {
        return "Password reset";
    }

    @Override
    public Map<String, Object> getProperties(BaseEmailDetailsDto emailDetailsDto) {
        if (!(emailDetailsDto instanceof ResetPasswordEmailDetailsDto dto)) {
            throw new IllegalArgumentException("Expected ResetPasswordEmailDetailsDto");
        }

        return Map.of(
                "name", dto.getName(),
                "resetLink", dto.getResetLink()
        );
    }

    @Override
    public String getTemplateName() {
        return "passwordReset.html";
    }

}
