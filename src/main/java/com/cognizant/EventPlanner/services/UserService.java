package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        }

    public List<UserResponseDto> getAllAdminUsers() {
        return userRepository.findByRole(Role.EVENT_ADMIN).stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public void demoteEventAdmin(Long adminUserId) {
        User admin = userRepository.findById(adminUserId).orElseThrow(() -> new EntityNotFoundException(User.class, adminUserId));
        if (admin.getRole() == Role.EVENT_ADMIN) {
            admin.setRole(Role.USER);
            userRepository.save(admin);
        } else {
            throw new EntityNotFoundException(User.class, adminUserId);
        }
    }
}
