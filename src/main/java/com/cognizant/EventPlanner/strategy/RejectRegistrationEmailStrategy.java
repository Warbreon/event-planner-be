package com.cognizant.EventPlanner.strategy;

import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import com.cognizant.EventPlanner.dto.email.EmailType;
import com.cognizant.EventPlanner.dto.email.private_event_registration.RejectRegistrationEmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RejectRegistrationEmailStrategy implements EmailStrategy  {
    @Override
    public EmailType getEmailType() {
        return EmailType.REGISTRATION_REJECTED;
    }

    @Override
    public String getSubject() {
        return "Event registration status update";
    }

    @Override
    public Map<String, Object> getProperties(BaseEmailDetailsDto emailDetailsDto) {
        if (!(emailDetailsDto instanceof RejectRegistrationEmailDto dto)) {
            throw new IllegalArgumentException("Expected RejectRegistrationEmailDto");
        }

        return Map.of(
                "name", dto.getName(),
                "eventName", dto.getEventName(),
                "eventCreatorFirstName", dto.getEventCreatorFirstName(),
                "eventCreatorLastName", dto.getEventCreatorLastName(),
                "eventCreatorEmail", dto.getEventCreatorEmail(),
                "eventPlannerUrl", dto.getEventPlannerUrl()
        );
    }

    @Override
    public String getTemplateName() {
        return EmailType.REGISTRATION_REJECTED.getTemplateName();
    }
}