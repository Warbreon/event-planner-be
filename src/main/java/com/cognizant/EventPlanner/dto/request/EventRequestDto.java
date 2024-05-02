package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.dto.EventDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventRequestDto extends EventDto {

    @NotNull(message = "Event must be created by existing user")
    @Min(value = 0, message = "User ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "User ID value can not exceed 9223372036854775807")
    private Long creatorId;

    @NotNull(message = "Attendees list is required. If there are no attendees, leave it empty")
    private Set<AttendeeRequestDto> attendees;

    @Min(value = 0, message = "Address ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Address ID value can not exceed 9223372036854775807")
    private Long addressId;

    private Set<Long> tagIds;
}
