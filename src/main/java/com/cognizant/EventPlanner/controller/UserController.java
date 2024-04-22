package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.exception.UnauthorizedException;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/admins")
    public ResponseEntity<List<UserResponseDto>> getAllAdminUsers(@RequestParam Long userId) {
        var user = userService.getUserById(userId);
        if(user.getRole() != Role.SYSTEM_ADMIN){
            throw new UnauthorizedException();
        }

        List<UserResponseDto> admins = userService.getAllAdminUsers();
        return ResponseEntity.ok(admins);
    }

    @PatchMapping("/admins/{id}")
    public void removeAdminUser(@RequestParam Long userId, @PathVariable (value = "id") Long adminId) {
        var user = userService.getUserById(userId);
        var adminToBeDemoted = userService.getUserById(adminId);
        if(user.getRole() != Role.SYSTEM_ADMIN || adminToBeDemoted.getRole() == Role.SYSTEM_ADMIN){
            throw new UnauthorizedException();
        }

        userService.removeAdminRole(adminId);
    }
}
