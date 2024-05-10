package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findAllById(Iterable<Long> ids);

}
