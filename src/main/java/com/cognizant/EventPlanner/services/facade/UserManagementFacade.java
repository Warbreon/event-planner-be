package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<UserResponseDto> getUsersByRoles(Optional<List<Role>> roles) {
        return roles.map(roleList -> userService.findUsersByRoles(roleList)
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList()))
                .orElseGet(() -> userService.findAllUsers().stream().map(userMapper::userToUserDto).toList());
    }

    public void demoteEventAdmins(List<Long> adminIds) {
        userService.demoteEventAdmins(adminIds);
    }

    public void promoteToEventAdmins(List<Long> userIds) { userService.promoteToEventAdmins(userIds); }
}
