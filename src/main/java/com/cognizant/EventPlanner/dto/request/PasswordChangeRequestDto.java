package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.validation.password.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 64, message = "Password cannot exceed 64 characters")
    private String newPassword;

}
