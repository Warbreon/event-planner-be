package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.PasswordResetToken;
import com.cognizant.EventPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

    void deleteByUserId(Long userId);

}
