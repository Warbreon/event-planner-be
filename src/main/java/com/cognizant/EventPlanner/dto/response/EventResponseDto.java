package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class EventResponseDto extends EventRequestDto {

    private Long id;
    private LocalDateTime createdDate;
    private Long creatorId;

}
