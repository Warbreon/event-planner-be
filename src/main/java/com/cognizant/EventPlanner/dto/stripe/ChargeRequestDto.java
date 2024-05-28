package com.cognizant.EventPlanner.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequestDto {

    private String token;
    private BigDecimal amount;
    private Long eventId;

}
