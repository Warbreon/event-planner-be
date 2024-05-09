package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserInfoResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.services.AttendeeService;
import com.cognizant.EventPlanner.services.UserDetailsServiceImpl;
import com.cognizant.EventPlanner.services.UserService;
import com.cognizant.EventPlanner.model.User;
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
    private final UserDetailsServiceImpl userDetailsService;
    private final AttendeeService attendeeService;

    public List<UserResponseDto> getUsers(Optional<List<Role>> roles) {
        List<User> users = roles.map(userService::findUsersByRoles).orElseGet(userService::findAllUsers);

        return users.stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public void demoteUser(List<Long> ids) {
        userService.changeUserRoles(ids, Role.EVENT_ADMIN, Role.USER);
    }

    public void promoteUser(List<Long> ids) {
        userService.changeUserRoles(ids, Role.USER, Role.EVENT_ADMIN);
    }

    public UserInfoResponseDto getUserInfo() {
        String email = userDetailsService.getCurrentUserEmail();
        User user = userService.findUserByEmail(email);
        int activeNotifications = attendeeService.countActiveNotifications(email);

        return new UserInfoResponseDto(
            user.getFirstName(),
            user.getImageUrl(),
            activeNotifications
        );
    }
}
