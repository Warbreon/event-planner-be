package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
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
    public ResponseEntity<List<UserResponseDto>> getAllAdminUsers() {
        List<UserResponseDto> admins = userService.getAllAdminUsers();
        return ResponseEntity.ok(admins);
    }

    @PatchMapping("/admins/demote/{id}")
    public ResponseEntity<Void> removeAdminUser(@PathVariable (value = "id") Long adminId) {
        userService.demoteEventAdmin(adminId);
        return ResponseEntity.ok().build();
    }
}
