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

    private String userFirstName;
    private String userImageUrl;
    private Integer notificationCount;

}
