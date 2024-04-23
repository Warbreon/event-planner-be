package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.model.PasswordResetToken;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String generateResetToken(User user) {
        String resetToken = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(resetToken);

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(hashedToken);
        token.setUser(user);
        token.setExpirationDate(LocalDateTime.now().plusHours(3));
        passwordResetTokenRepository.save(token);

        return resetToken;
    }

}
