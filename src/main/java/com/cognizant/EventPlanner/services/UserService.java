package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<User> listOfUsers = new ArrayList<>();
        for (Role role : roleList) {
            listOfUsers.addAll(userRepository.findByRole(role));
        }
        return listOfUsers;
    }

    @Transactional
    public void demoteEventAdmins(List<Long> adminUserIds) {
        userRepository.updateRolesById(adminUserIds, Role.EVENT_ADMIN, Role.USER);
    }

    @Transactional
    public void promoteToEventAdmins(List<Long> userIds) {
        userRepository.updateRolesById(userIds, Role.USER, Role.EVENT_ADMIN);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUserRegistered(Event event, String userEmail) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getEmail().equals(userEmail));
    }
}
