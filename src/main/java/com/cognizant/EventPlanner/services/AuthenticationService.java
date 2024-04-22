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

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getEmail());
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
        Role role = jwtTokenUtil.convertAuthoritiesToRole(userDetails.getAuthorities());

        return new AuthenticationResponse(accessToken, refreshToken, userDetails.getUsername(), role);
    }

    public AuthenticationResponse refreshAccessToken(TokenRefreshRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        jwtTokenUtil.validateToken(refreshToken);
        String email = jwtTokenUtil.getEmailFromToken(refreshToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        Role role = jwtTokenUtil.convertAuthoritiesToRole(userDetails.getAuthorities());

        return new AuthenticationResponse(newAccessToken, refreshToken, userDetails.getUsername(), role);
    }


    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
