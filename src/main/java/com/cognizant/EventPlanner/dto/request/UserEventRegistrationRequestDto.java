package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEventRegistrationRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Event ID is required")
    @Min(value = 0, message = "Event ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Event ID value can not exceed 9223372036854775807")
    private Long eventId;

}
