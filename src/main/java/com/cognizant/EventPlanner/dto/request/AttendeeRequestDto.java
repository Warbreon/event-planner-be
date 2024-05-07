package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeRequestDto {
    @NotNull(message = "Only an existing user can be an attendee")
    @Min(value = 0, message = "User ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "User ID value can not exceed 9223372036854775807")
    private Long userId;

    @NotNull(message = "Attendee can register only to existing events")
    @Min(value = 0, message = "Event ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Event ID value can not exceed 9223372036854775807")
    private Long eventId;
}
