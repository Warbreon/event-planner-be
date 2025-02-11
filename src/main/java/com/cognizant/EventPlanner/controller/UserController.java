package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserInfoResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.services.facade.UserManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/demoteAdmins/{ids}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> demoteAdminUsers(@PathVariable List<Long> ids) {
        userManagementFacade.demoteUser(ids);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/promoteAdmins/{ids}")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<Void> promoteUsersToEventAdmins(@PathVariable List<Long> ids) {
        userManagementFacade.promoteUser(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current/info")
    public ResponseEntity<UserInfoResponseDto> getUserProfile() {
        UserInfoResponseDto response = userManagementFacade.getUserInfo();
        return ResponseEntity.ok(response);
    }
}
