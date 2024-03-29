package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.model.Event;
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

    public static EventResponseDto of(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .isOpen(event.isOpen())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .registrationStart(event.getRegistrationStart())
                .registrationEnd(event.getRegistrationEnd())
                .agenda(event.getAgenda())
                .price(event.getPrice())
                .inviteUrl(event.getInviteUrl())
                .addressId(event.getAddress() != null ? event.getAddress().getId() : null)
                .createdDate(event.getCreatedDate())
                .creatorId(event.getCreator().getId())
                .build();
    }
}
