package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class, email));
    }

    public List<User> findUsersByRoles(List<Role> roleList) {
        return userRepository.findByRoleIn(roleList);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUserRegistered(Event event, String userEmail) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getEmail().equals(userEmail));
    }

    @Transactional
    public void changeUserRoles(List<Long> ids, Role prevRole, Role newRole) {
        userRepository.updateRolesById(ids, prevRole, newRole);
    }
}
