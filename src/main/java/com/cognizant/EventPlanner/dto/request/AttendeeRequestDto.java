package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeRequestDto extends BaseEventRegistrationRequestDto {

    @NotNull(message = "Only an existing user can be an attendee")
    @Min(value = 0, message = "User ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "User ID value can not exceed 9223372036854775807")
    private Long userId;

}
