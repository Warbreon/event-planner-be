package com.cognizant.EventPlanner.dto.request;

import com.cognizant.EventPlanner.model.Currency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditEventRequestDto {

    @Size(max = 255, message = "Event name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Boolean isOpen;


    private LocalDateTime eventStart;


    private LocalDateTime eventEnd;


    private LocalDateTime registrationStart;


    private LocalDateTime registrationEnd;

    private String[] agenda;

    @DecimalMin(value = "0.00", message = "Event price can not be less than 0.00")
    @DecimalMax(value = "10000.00", message = "Event price can not be greater than 10000.00")
    private BigDecimal price;

    private Currency currency;

    @Min(value = 0, message = "Event tickets count can not be less than 0")
    @Max(value = 10000, message = "Event tickets count can not be greater than 10000")
    private Integer tickets;

    @Size(max = 255, message = "Invite to online meeting cannot exceed 255 characters")
    private String inviteUrl;

    private Set<Long> attendeeIds;

    @Min(value = 0, message = "Address ID has to be equal or greater than 0")
    @Max(value = Long.MAX_VALUE, message = "Address ID value can not exceed 9223372036854775807")
    private Long addressId;

    private Set<Long> tagIds;

    private String imageBase64;

    private String cardImageBase64;
}
