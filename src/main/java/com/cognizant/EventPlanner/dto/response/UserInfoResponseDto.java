package com.cognizant.EventPlanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userImageUrl;
    private Integer notificationCount;

}
