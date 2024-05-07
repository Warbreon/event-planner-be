package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserProfileResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.facade.UserManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementFacade userManagementFacade;

    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserAsAttendeeResponseDto>> getAllUsers() {
        List<UserAsAttendeeResponseDto> response = userManagementFacade.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllAdminUsers() {
        List<Role> adminRoles = Arrays.asList(Role.SYSTEM_ADMIN, Role.EVENT_ADMIN);
        List<UserResponseDto> admins = userManagementFacade.getUsersByRoles(adminRoles);
        return ResponseEntity.ok(admins);
    }

    @PatchMapping("/admins/demote/{id}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> demoteAdminUser(@PathVariable(value = "id") Long adminId) {
        userManagementFacade.demoteEventAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admins/promote/{id}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> promoteUserToAdmin(@PathVariable(value = "id") Long userId) {
        userManagementFacade.promoteToEventAdmin(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current/info")
    public ResponseEntity<UserProfileResponseDto> getUserProfile() {
        UserProfileResponseDto response = userManagementFacade.getUserProfile();
        return ResponseEntity.ok(response);
    }
}
