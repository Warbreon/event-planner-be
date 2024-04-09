package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.model.PaymentStatus;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeResponseDto {

    private Long id;
    private RegistrationStatus registrationStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime registrationTime;
    private Boolean isNewNotification;
    private UserAsAttendeeResponseDto user;
    @Setter
    private String message;

    public AttendeeResponseDto(String message) {
        this.message = message;
    }
}
