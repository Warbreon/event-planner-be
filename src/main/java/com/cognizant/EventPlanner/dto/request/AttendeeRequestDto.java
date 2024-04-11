package com.cognizant.EventPlanner.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long eventId;

}
