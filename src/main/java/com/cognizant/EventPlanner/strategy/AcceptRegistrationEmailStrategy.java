package com.cognizant.EventPlanner.strategy;

import com.cognizant.EventPlanner.dto.email.private_event_registration.AcceptRegistrationEmailDto;
import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import com.cognizant.EventPlanner.dto.email.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AcceptRegistrationEmailStrategy implements EmailStrategy {
    @Override
    public EmailType getEmailType() {
        return EmailType.REGISTRATION_ACCEPTED;
    }

    @Override
    public String getSubject() {
        return "Congratulations! You are going!";
    }

    @Override
    public Map<String, Object> getProperties(BaseEmailDetailsDto emailDetailsDto) {
        if (!(emailDetailsDto instanceof AcceptRegistrationEmailDto dto)) {
            throw new IllegalArgumentException("Expected RegistrationResponseEmailDto");
        }

        return Map.of(
                "name", dto.getName(),
                "eventId", dto.getEventId(),
                "eventName", dto.getEventName(),
                "eventStartDate", dto.getEventStartDate(),
                "eventStartTime", dto.getEventStartTime(),
                "eventCreatorFirstName", dto.getEventCreatorFirstName(),
                "eventCreatorLastName", dto.getEventCreatorLastName(),
                "eventCreatorEmail", dto.getEventCreatorEmail(),
                "eventUrl", dto.getEventUrl(),
                "eventImageUrl", dto.getEventImageUrl()
        );
    }

    @Override
    public String getTemplateName() {
        return EmailType.REGISTRATION_ACCEPTED.getTemplateName();
    }
}