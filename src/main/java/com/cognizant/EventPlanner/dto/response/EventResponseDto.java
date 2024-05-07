package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.dto.EventDto;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventResponseDto extends EventDto {

    private Long id;
    private LocalDateTime createdDate;
    private Long creatorId;
    private AddressResponseDto address;
    private Set<AttendeeResponseDto> attendees;
    private RegistrationStatus currentUserRegistrationStatus;
    private Set<TagResponseDto> tags;

}
