package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.model.PasswordResetToken;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${reset.token.expiration:3600}")
    private long resetTokenExpiration;

    public String generateResetToken(User user) {
        try {
            String resetToken = UUID.randomUUID().toString();
            String hashedToken = passwordEncoder.encode(resetToken);

            PasswordResetToken token = new PasswordResetToken();
            token.setToken(hashedToken);
            token.setUser(user);
            token.setExpirationDate(LocalDateTime.now().plusSeconds(resetTokenExpiration));
            passwordResetTokenRepository.save(token);

            return hashedToken;
        } catch (Exception ex) {
            log.error("Error generating token for user: {}", user.getId(), ex);
            throw ex;
        }
    }

}
