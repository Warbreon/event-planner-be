package com.cognizant.EventPlanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String jobTitle;
    private String phoneNumber;
    private String email;
    private String imageUrl;
    private String password;

}
