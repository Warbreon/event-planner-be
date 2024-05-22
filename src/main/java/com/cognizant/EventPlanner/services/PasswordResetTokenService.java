package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.config.properties.PasswordResetTokenProperties;
import com.cognizant.EventPlanner.exception.passwordReset.TokenExpiredException;
import com.cognizant.EventPlanner.exception.passwordReset.TokenNotFoundException;
import com.cognizant.EventPlanner.model.PasswordResetToken;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private static final int VALID_REMAINING_TOKEN_EXPIRY_TIME = 10;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenProperties passwordResetTokenProperties;

    public String generateResetToken(User user) {
        try {
            Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUser(user);

            if (existingToken.isPresent()) {
                PasswordResetToken token = existingToken.get();

                if (!isTokenExpired(token) && isRemainingTokenExpiryAcceptable(token)) {
                    return token.getToken();
                }

                passwordResetTokenRepository.delete(token);
            }

            String hashedToken = createNewToken();
            saveResetToken(hashedToken, user);

            return hashedToken;
        } catch (Exception ex) {
            log.error("Error generating token for user: {}", user.getId(), ex);
            throw ex;
        }
    }

    public User validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Reset token not found"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired");
        }

        return resetToken.getUser();
    }

    public void deleteUserToken(User user) {
        passwordResetTokenRepository.deleteByUserId(user.getId());
    }

    private void saveResetToken(String hashedToken, User user) {
        try {
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(hashedToken);
            token.setUser(user);
            token.setExpirationDate(LocalDateTime.now().plusSeconds(passwordResetTokenProperties.getExpiration()));
            passwordResetTokenRepository.save(token);
        } catch (Exception ex) {
            log.error("Error saving token for user: {}", user.getId(), ex);
            throw new RuntimeException("Failed to save reset token", ex);
        }
    }

    private String createNewToken() {
        String resetToken = UUID.randomUUID().toString();
        return passwordEncoder.encode(resetToken);
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpirationDate().isBefore(LocalDateTime.now());
    }

    private boolean isRemainingTokenExpiryAcceptable(PasswordResetToken token) {
        LocalDateTime thresholdTime = LocalDateTime.now().plusMinutes(VALID_REMAINING_TOKEN_EXPIRY_TIME);
        return token.getExpirationDate().isAfter(thresholdTime);
    }

}
