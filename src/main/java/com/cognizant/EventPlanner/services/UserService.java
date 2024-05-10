package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public void demoteEventAdmin(Long adminUserId) {
        User admin = findUserById(adminUserId);
        if (admin.getRole() == Role.EVENT_ADMIN) {
            admin.setRole(Role.USER);
            userRepository.save(admin);
        }
    }

    public void promoteToEventAdmin(Long userId) {
        User newAdmin = findUserById(userId);
        if (newAdmin.getRole() == Role.USER) {
            newAdmin.setRole(Role.EVENT_ADMIN);
            userRepository.save(newAdmin);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersByIds(Set<Long> ids) {
        return userRepository.findAllById(ids);
    }

}
