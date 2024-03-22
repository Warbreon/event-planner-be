package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    
    private String jwtToken;
    private String email;
    private Role role;

}
