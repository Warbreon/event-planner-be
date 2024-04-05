package com.cognizant.EventPlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EventDto {

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

}
