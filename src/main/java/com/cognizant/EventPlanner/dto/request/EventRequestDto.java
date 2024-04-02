package com.cognizant.EventPlanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EventRequestDto {

    private String name;
    private String description;
    private String imageUrl;
    private boolean isOpen;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
    private String[] agenda;
    private Double price;
    private String inviteUrl;
    private Long addressId;

}
