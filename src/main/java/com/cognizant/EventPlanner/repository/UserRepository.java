package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.role != 'USER'")
    List<User> findAllAdminUsers();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = 'USER' WHERE u.id = :userId")
    void removeAdminRoleForUser(long userId);
}
