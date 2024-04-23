package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
public class PasswordResetRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Reset link cannot be blank")
    @URL(message = "Reset link should be URL")
    private String resetLinkBase;

}
