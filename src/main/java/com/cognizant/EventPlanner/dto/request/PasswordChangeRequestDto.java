package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.validation.password.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    @StrongPassword
    private String newPassword;

}
