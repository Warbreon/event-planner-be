package com.cognizant.EventPlanner.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequestDto {

    private String chargeId;
    private Long attendeeId;

}
