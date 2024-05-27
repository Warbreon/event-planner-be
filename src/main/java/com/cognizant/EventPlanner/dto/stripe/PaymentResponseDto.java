package com.cognizant.EventPlanner.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private boolean isSuccess;
    private String message;
    private String transactionId;
    private int httpStatus;

}
