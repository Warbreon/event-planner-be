package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.UserMapper;
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

    public UserResponseDto getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        return convertUserToDto(user);
    }

    public List<UserResponseDto> getAllAdminUsers() {
        return userRepository.findAllAdminUsers().stream().map(this::convertUserToDto).collect(Collectors.toList());
    }

    public void removeAdminRole(Long adminUserId) {
        userRepository.removeAdminRoleForUser(adminUserId);
    }

    private UserResponseDto convertUserToDto(User user) {
        return userMapper.userToUserDto(user);
    }
}
