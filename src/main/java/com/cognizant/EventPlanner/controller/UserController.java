package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasAnyAuthority('EVENT_ADMIN', 'SYSTEM_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>>getAllUsers(){
        List<UserResponseDto> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
