package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.email.ResetPasswordEmailDetailsDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.AuthenticationService;
import com.cognizant.EventPlanner.services.EmailService;
import com.cognizant.EventPlanner.services.UserService;
import com.cognizant.EventPlanner.util.EmailDetailsBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody PasswordResetRequestDto request) {
        User user = userService.findUserByEmail(request.getEmail());
        String resetToken = authenticationService.generateResetToken(user);
        ResetPasswordEmailDetailsDto emailDetails = EmailDetailsBuilder.buildResetPasswordDetails(user, request,
                resetToken);
        emailService.sendEmail(emailDetails);

        return ResponseEntity.ok("Password reset email sent.");
    }

}
