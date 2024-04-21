package com.cognizant.EventPlanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTagRequestDto {

    private Long tagId;
    private Long eventId;
}
