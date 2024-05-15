package com.cognizant.EventPlanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {

    private List<AttendeeNotificationResponseDto> attendeeNotifications;
    private int totalNotifications;
    private int activeNotifications;
}
