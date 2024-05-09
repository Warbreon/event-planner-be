package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserInfoResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.facade.UserManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementFacade userManagementFacade;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'EVENT_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestParam(required = false) List<Role> roles) {
        List<UserResponseDto> admins = userManagementFacade.getUsers(Optional.ofNullable(roles));
        return ResponseEntity.ok(admins);
    }

    @PatchMapping("/changeRole")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> demoteAdminUsers(@RequestParam List<Long> ids, @RequestParam Role prevRole, @RequestParam Role newRole) {
        userManagementFacade.changeUserRoles(ids, prevRole, newRole);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current/info")
    public ResponseEntity<UserInfoResponseDto> getUserProfile() {
        UserInfoResponseDto response = userManagementFacade.getUserInfo();
        return ResponseEntity.ok(response);
    }
}
