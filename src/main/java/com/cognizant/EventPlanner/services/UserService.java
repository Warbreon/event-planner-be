package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.exception.passwordReset.PasswordReuseException;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class, email));
    }
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    public List<User> findUsersByRoles(List<Role> roleList) {
        return userRepository.findByRoleIn(roleList);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersByIds(Set<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Transactional
    public void changeUserRoles(List<Long> ids, Role prevRole, Role newRole) {
        userRepository.updateRolesById(ids, prevRole, newRole);
    }

    public void updatePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new PasswordReuseException();
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
    }

}
