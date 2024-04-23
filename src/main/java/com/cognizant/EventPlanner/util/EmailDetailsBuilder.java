package com.cognizant.EventPlanner.util;

import com.cognizant.EventPlanner.dto.email.EmailType;
import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
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

}
