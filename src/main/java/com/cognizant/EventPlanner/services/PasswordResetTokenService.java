package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.config.properties.ResetTokenProperties;
import com.cognizant.EventPlanner.model.PasswordResetToken;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenProperties resetTokenProperties;

    public String generateResetToken(User user) {
        try {
            String resetToken = UUID.randomUUID().toString();
            String hashedToken = passwordEncoder.encode(resetToken);
            saveResetToken(hashedToken, user);
            return hashedToken;
        } catch (Exception ex) {
            log.error("Error generating token for user: {}", user.getId(), ex);
            throw ex;
        }
    }

    private void saveResetToken(String hashedToken, User user) {
        try {
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(hashedToken);
            token.setUser(user);
            token.setExpirationDate(LocalDateTime.now().plusSeconds(resetTokenProperties.getExpiration()));
            passwordResetTokenRepository.save(token);
        } catch (Exception ex) {
            log.error("Error saving token for user: {}", user.getId(), ex);
            throw new RuntimeException("Failed to save reset token", ex);
        }
    }

}
