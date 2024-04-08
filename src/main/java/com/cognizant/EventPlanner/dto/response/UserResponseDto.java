package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String phoneNumber;
    private String email;
    private String imageUrl;
    private Role role;

}
