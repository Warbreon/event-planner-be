package com.cognizant.EventPlanner.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cognizant.EventPlanner.dto.request.JwtRequest;
import com.cognizant.EventPlanner.dto.response.JwtResponse;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.security.jwt.JwtTokenUtil;
import com.cognizant.EventPlanner.services.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest request) {
        authenticate(request.getEmail(), request.getPassword());

        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Role role = authorities.isEmpty() ? null : convertStringToRole(authorities.iterator().next().getAuthority());
        
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername(), role));
    }

    @GetMapping("/lol")
    public ResponseEntity<String> lol() {
        return ResponseEntity.ok("sVVAVAVAVAVAVV");
    }

    private Role convertStringToRole(String authority) {
        try {
            return Role.valueOf(authority);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unexpected role value: " + authority);
        }
    }
    
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
