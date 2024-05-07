package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public List<UserAsAttendeeResponseDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> getUsersByRoles(List<Role> adminRoles) {
        return userService.findUsersByRoles(adminRoles)
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public void demoteEventAdmin(Long adminId) {
        userService.demoteEventAdmin(adminId);
    }

    public void promoteToEventAdmin(Long userId) { userService.promoteToEventAdmin(userId); }
}
