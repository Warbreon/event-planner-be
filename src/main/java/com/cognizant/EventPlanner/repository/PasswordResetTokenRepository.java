package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
}
