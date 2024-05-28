package com.cognizant.EventPlanner.util;

import com.cognizant.EventPlanner.dto.email.private_event_registration.AcceptRegistrationEmailDto;
import com.cognizant.EventPlanner.dto.email.EmailType;
import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import com.cognizant.EventPlanner.dto.email.private_event_registration.RejectRegistrationEmailDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;

public class EmailDetailsBuilder {

    public static ResetPasswordEmailDetailsDto buildResetPasswordDetails(
            User user, PasswordResetRequestDto requestDto, String resetToken) {
        if (user == null || requestDto == null || resetToken == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        String resetLink = requestDto.getResetLinkBase() + "/" + resetToken;

        return ResetPasswordEmailDetailsDto.builder()
                .recipientEmail(requestDto.getEmail())
                .resetLink(resetLink)
                .name(user.getFirstName())
                .emailType(EmailType.PASSWORD_RESET)
                .build();
    }

    public static AcceptRegistrationEmailDto acceptRegistrationEmailBuilder(User user, Event event, User eventCreator) {
        handleNullParametersException(user, event, eventCreator);

        return AcceptRegistrationEmailDto.builder()
                .recipientEmail(user.getEmail())
                .name(user.getFirstName())
                .eventId(event.getId())
                .eventName(event.getName())
                .eventStartDate(event.getEventStart().toLocalDate())
                .eventStartTime(event.getEventStart().toLocalTime())
                .eventCreatorFirstName(eventCreator.getFirstName())
                .eventCreatorLastName(eventCreator.getLastName())
                .eventCreatorEmail(eventCreator.getEmail())
                .eventUrl("https://raisav.devbstaging.com/events/event/" + event.getId())
                .eventImageUrl(event.getImageUrl())
                .emailType(EmailType.REGISTRATION_ACCEPTED)
                .build();
    }

    public static RejectRegistrationEmailDto rejectRegistrationEmailBuilder(User user, Event event, User eventCreator) {
        handleNullParametersException(user, event, eventCreator);

        return RejectRegistrationEmailDto.builder()
                .recipientEmail(user.getEmail())
                .name(user.getFirstName())
                .eventName(event.getName())
                .eventCreatorFirstName(eventCreator.getFirstName())
                .eventCreatorLastName(eventCreator.getLastName())
                .eventCreatorEmail(eventCreator.getEmail())
                .eventPlannerUrl("https://raisav.devbstaging.com")
                .emailType(EmailType.REGISTRATION_REJECTED)
                .build();
    }

    private static void handleNullParametersException(User user, Event event, User eventCreator) {
        if (user == null || event == null || eventCreator == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
    }
}
