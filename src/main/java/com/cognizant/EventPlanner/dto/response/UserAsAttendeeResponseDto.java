package com.cognizant.EventPlanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAsAttendeeResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String imageUrl;

}
