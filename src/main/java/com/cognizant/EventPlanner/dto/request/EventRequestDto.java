package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.dto.EventDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventRequestDto extends EventDto {

    @NotNull(message = "Attendees list is required. If there are no attendees, leave it empty")
    private Set<Long> attendeeIds;

    @Min(value = 0, message = "Address ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Address ID value can not exceed 9223372036854775807")
    private Long addressId;

    @NotNull(message = "Tag IDs list is required. If there are no tags, leave it empty")
    private Set<Long> tagIds;

    @NotBlank(message = "Event must have an image")
    private String imageBase64;

    @NotBlank(message = "Event must have a card image")
    private String cardImageBase64;
}
