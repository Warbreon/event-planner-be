package com.cognizant.EventPlanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeRequestDto {

    private Long userId;
    private Long eventId;

}
