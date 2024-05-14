package com.cognizant.EventPlanner.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {

    PASSWORD_RESET("PasswordReset.html");

    private final String templateName;

}
