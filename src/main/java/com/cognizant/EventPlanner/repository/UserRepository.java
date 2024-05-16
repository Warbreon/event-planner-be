package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleIn(List<Role> roles);

    @Modifying
    @Query("UPDATE User u SET u.role = :newRole WHERE u.id IN :ids AND u.role = :previousRole")
    void updateRolesById(List<Long> ids, Role previousRole, Role newRole);
}
