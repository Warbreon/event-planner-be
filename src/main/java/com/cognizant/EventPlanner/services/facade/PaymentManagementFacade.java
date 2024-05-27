package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.stripe.ChargeRequestDto;
import com.cognizant.EventPlanner.dto.stripe.PaymentResponseDto;
import com.cognizant.EventPlanner.dto.stripe.RefundRequestDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.PaymentStatus;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import com.cognizant.EventPlanner.services.AttendeeService;
import com.cognizant.EventPlanner.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentManagementFacade {

    private final StripeService stripeService;
    private final AttendeeService attendeeService;

    @Transactional
    public PaymentResponseDto processPayment(ChargeRequestDto chargeRequestDto) {
        try {
            Charge charge = stripeService.createCharge(chargeRequestDto);
            Attendee attendee = attendeeService.findAttendeeById(chargeRequestDto.getAttendeeId());
            attendee.setPaymentStatus(PaymentStatus.PAID);
            attendee.setRegistrationStatus(RegistrationStatus.ACCEPTED);
            attendeeService.saveAttendee(attendee);
            return new PaymentResponseDto(true, "Payment successful", charge.getId(), HttpStatus.OK.value());
        } catch (StripeException ex) {
            Attendee attendee = attendeeService.findAttendeeById(chargeRequestDto.getAttendeeId());
            attendee.setPaymentStatus(PaymentStatus.FAILED);
            attendeeService.saveAttendee(attendee);
            return new PaymentResponseDto(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @Transactional
    public PaymentResponseDto refundPayment(RefundRequestDto refundRequestDto) {
        try {
            Refund refund = stripeService.refundCharge(refundRequestDto);
            Attendee attendee = attendeeService.findAttendeeById(refundRequestDto.getAttendeeId());
            attendee.setPaymentStatus(PaymentStatus.REFUNDED);
            attendeeService.saveAttendee(attendee);
            return new PaymentResponseDto(true, "Refund successful", refund.getId(), HttpStatus.OK.value());
        } catch (StripeException ex) {
            Attendee attendee = attendeeService.findAttendeeById(refundRequestDto.getAttendeeId());
            attendee.setPaymentStatus(PaymentStatus.NOT_REFUNDED);
            attendeeService.saveAttendee(attendee);
            return new PaymentResponseDto(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        }
    }

}
