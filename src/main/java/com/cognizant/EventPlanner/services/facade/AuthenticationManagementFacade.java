package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.EmailService;
import com.cognizant.EventPlanner.services.PasswordResetTokenService;
import com.cognizant.EventPlanner.services.UserService;
import com.cognizant.EventPlanner.util.EmailDetailsBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationManagementFacade {

    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final UserService userService;

    public void handlePasswordReset(PasswordResetRequestDto requestDto) {
        User user = userService.findUserByEmail(requestDto.getEmail());
        String resetToken = passwordResetTokenService.generateResetToken(user);
        ResetPasswordEmailDetailsDto emailDetails = EmailDetailsBuilder.buildResetPasswordDetails(
                user,
                requestDto,
                resetToken
        );
        emailService.sendEmail(emailDetails);
    }

}
