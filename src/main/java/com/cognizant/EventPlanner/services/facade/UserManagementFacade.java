package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserInfoResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.EventService;
import com.cognizant.EventPlanner.services.UserDetailsServiceImpl;
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
    private final UserDetailsServiceImpl userDetailsService;
    private final EventService eventService;

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

    public void promoteToEventAdmin(Long userId) {
        userService.promoteToEventAdmin(userId);
    }

    public UserInfoResponseDto getUserInfo() {
        String email = userDetailsService.getCurrentUserEmail();
        User user = userService.findUserByEmail(email);
        int activeNotifications = eventService.countActiveNotifications(email);

        return new UserInfoResponseDto(
            user.getFirstName(),
            user.getImageUrl(),
            activeNotifications
        );
    }
}
