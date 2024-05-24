package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import com.cognizant.EventPlanner.dto.request.PasswordChangeRequestDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.EmailService;
import com.cognizant.EventPlanner.services.PasswordResetTokenService;
import com.cognizant.EventPlanner.services.UserService;
import com.cognizant.EventPlanner.util.EmailDetailsBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationManagementFacade {

    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final UserService userService;

    @Transactional
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

    @Transactional
    public void handlePasswordChange(PasswordChangeRequestDto request) {
        User user = passwordResetTokenService.validateResetToken(request.getToken());
        userService.updatePassword(user, request.getNewPassword());
        passwordResetTokenService.deleteUserToken(user);
    }

}
