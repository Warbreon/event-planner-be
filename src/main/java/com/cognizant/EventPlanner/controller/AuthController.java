package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.PasswordChangeRequestDto;
import com.cognizant.EventPlanner.dto.request.PasswordResetRequestDto;
import com.cognizant.EventPlanner.services.facade.AuthenticationManagementFacade;
import com.cognizant.EventPlanner.dto.request.AuthenticationRequest;
import com.cognizant.EventPlanner.dto.request.TokenRefreshRequest;
import com.cognizant.EventPlanner.dto.response.AuthenticationResponse;
import com.cognizant.EventPlanner.services.AuthenticationService;
import jakarta.validation.Valid;
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
    private final AuthenticationManagementFacade authenticationManagementFacade;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(@Valid @RequestBody TokenRefreshRequest request) {
        AuthenticationResponse response = authenticationService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> requestResetPassword(@Valid @RequestBody PasswordResetRequestDto request) {
        authenticationManagementFacade.handlePasswordReset(request);
        return ResponseEntity.ok("Password reset email sent.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequestDto request) {
        authenticationManagementFacade.handlePasswordChange(request);
        return ResponseEntity.ok("Password has been updated successfully.");
    }

}
