package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.request.AuthenticationRequest;
import com.cognizant.EventPlanner.dto.request.TokenRefreshRequest;
import com.cognizant.EventPlanner.dto.response.AuthenticationResponse;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.security.jwt.JwtTokenUtil;
import com.cognizant.EventPlanner.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest request) {
        authenticate(request.getEmail(), request.getPassword());

        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());

        Role role = jwtTokenUtil.convertAuthoritiesToRole(userDetails.getAuthorities());
        
        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken, userDetails.getUsername(), role));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody TokenRefreshRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        jwtTokenUtil.validateToken(refreshToken);

        String email = jwtTokenUtil.getEmailFromToken(refreshToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());

        Role role = jwtTokenUtil.convertAuthoritiesToRole(userDetails.getAuthorities());

        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken, userDetails.getUsername(), role));
    }

    /**
     * For Users check PgAdmin or Database extension if you have IDEA Ultimate
     * {
     *     "email": "guy.hawkins@gmail.com",
     *     "password": "password"
     * }
     * @PreAuthorize to check roles. If role is not in authority, then it will throw 403
     */
    @GetMapping("/test-response")
    @Profile("dev")
    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    public ResponseEntity<String> testResponse() {
        return ResponseEntity.ok("Some text returned");
    }
    
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
