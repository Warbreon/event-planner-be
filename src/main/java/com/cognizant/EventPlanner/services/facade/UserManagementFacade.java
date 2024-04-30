package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.mapper.UserMapper;
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
}
