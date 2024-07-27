package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.stripe.ChargeRequestDto;
import com.cognizant.EventPlanner.dto.stripe.PaymentResponseDto;
import com.cognizant.EventPlanner.dto.stripe.RefundRequestDto;
import com.cognizant.EventPlanner.services.facade.PaymentManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentManagementFacade paymentManagementFacade;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody ChargeRequestDto chargeRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentManagementFacade.processPayment(chargeRequestDto);
        return ResponseEntity.status(paymentResponseDto.getHttpStatus()).body(paymentResponseDto);
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponseDto> refundPayment(@RequestBody RefundRequestDto refundRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentManagementFacade.refundPayment(refundRequestDto);
        return ResponseEntity.status(paymentResponseDto.getHttpStatus()).body(paymentResponseDto);
    }

}
