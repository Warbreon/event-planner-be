package com.cognizant.EventPlanner.services;
import com.cognizant.EventPlanner.dto.request.AuthenticationRequest;
import com.cognizant.EventPlanner.dto.request.TokenRefreshRequest;
import com.cognizant.EventPlanner.dto.response.AuthenticationResponse;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        return buildAuthenticationResponse(authenticationRequest.getEmail(), null);
    }

    public AuthenticationResponse refreshAccessToken(TokenRefreshRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        jwtTokenUtil.validateToken(refreshToken);
        String email = jwtTokenUtil.getEmailFromToken(refreshToken);
        return buildAuthenticationResponse(email, refreshToken);
    }

    private AuthenticationResponse buildAuthenticationResponse(String email, String refreshToken)
    {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());

        if (refreshToken == null) {
            refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
        }

        Role role = jwtTokenUtil.convertAuthoritiesToRole(userDetails.getAuthorities());
        return new AuthenticationResponse(accessToken, refreshToken, userDetails.getUsername(), role);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
