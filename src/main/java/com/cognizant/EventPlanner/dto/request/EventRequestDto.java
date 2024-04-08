package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.dto.EventDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventRequestDto extends EventDto {

    private Long addressId;

}
