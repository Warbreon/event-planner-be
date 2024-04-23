package com.cognizant.EventPlanner.dto.request;

import lombok.Getter;

@Getter
public class PasswordResetRequestDto {

    private String email;
    private String resetLinkBase;

}
