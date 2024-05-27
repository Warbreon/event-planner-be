package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.stripe.ChargeRequestDto;
import com.cognizant.EventPlanner.dto.stripe.RefundRequestDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    public Charge createCharge(ChargeRequestDto chargeRequestDto) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (chargeRequestDto.getAmount().doubleValue() * 100));
        chargeParams.put("currency", "eur");
        chargeParams.put("source", chargeRequestDto.getToken());
        return Charge.create(chargeParams);
    }

    public Refund refundCharge(RefundRequestDto refundRequestDto) throws StripeException {
        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("charge", refundRequestDto.getChargeId());
        return Refund.create(refundParams);
    }
}
