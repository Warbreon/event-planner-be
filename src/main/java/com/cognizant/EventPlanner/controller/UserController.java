package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/admins")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllAdminUsers() {
        List<Role> adminRoles = Arrays.asList(Role.SYSTEM_ADMIN, Role.EVENT_ADMIN);
        List<UserResponseDto> admins = userService.findUsersByRoles(adminRoles);
        return ResponseEntity.ok(admins);
    }

    @PatchMapping("/admins/demote/{id}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> demoteAdminUser(@PathVariable (value = "id") Long adminId) {
        userService.demoteEventAdmin(adminId);
        return ResponseEntity.noContent().build();
    }
}
