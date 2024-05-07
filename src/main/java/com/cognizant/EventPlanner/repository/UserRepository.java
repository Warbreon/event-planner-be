package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = ?3 WHERE u.id IN ?1 AND u.role = ?2")
    void updateRolesById(List<Long> id, Role previousRole, Role newRole);
}
