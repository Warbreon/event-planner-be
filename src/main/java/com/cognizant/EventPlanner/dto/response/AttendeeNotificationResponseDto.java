package com.cognizant.EventPlanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeNotificationResponseDto extends AttendeeResponseDto{

    private Long eventId;
    private String eventName;
    private LocalDateTime eventStart;

}
