package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.controller.NotificationController;
import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.stripe.ChargeRequestDto;
import com.cognizant.EventPlanner.dto.stripe.PaymentResponseDto;
import com.cognizant.EventPlanner.dto.stripe.RefundRequestDto;
import com.cognizant.EventPlanner.model.*;
import com.cognizant.EventPlanner.services.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentManagementFacade {

    private final StripeService stripeService;
    private final RegistrationService registrationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final EventService eventService;
    private final AttendeeService attendeeService;
    private final NotificationController notificationController;
    private final TransactionService transactionService;

    @Transactional
    public PaymentResponseDto processPayment(ChargeRequestDto chargeRequestDto) {
        try {
            Charge charge = stripeService.createCharge(chargeRequestDto);

            String userEmail = userDetailsService.getCurrentUserEmail();
            User user = userService.findUserByEmail(userEmail);
            Event event = eventService.findEventById(chargeRequestDto.getEventId());

            Optional<Attendee> existingAttendee = attendeeService.findAttendeeByUserAndEvent(user.getId(), event.getId());

            if (existingAttendee.isPresent() && existingAttendee.get().getPaymentStatus().equals(PaymentStatus.PAID)) {
                return new PaymentResponseDto(false, "Attendee already registered", null, HttpStatus.BAD_REQUEST.value());
            } else if (existingAttendee.isPresent() && existingAttendee.get().getRegistrationStatus().equals(RegistrationStatus.REJECTED)) {
                return new PaymentResponseDto(false, "Attendee rejected", null, HttpStatus.BAD_REQUEST.value());
            }

            AttendeeRequestDto attendeeRequest = new AttendeeRequestDto();
            attendeeRequest.setUserId(user.getId());
            attendeeRequest.setEventId(event.getId());

            Attendee attendeeToRegister = registrationService.createAttendee(attendeeRequest, user, event);
            registrationService.updateAttendeeStatuses(attendeeToRegister, event, event.getIsOpen() ? RegistrationStatus.ACCEPTED : RegistrationStatus.PENDING, PaymentStatus.PAID);
            Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);

            transactionService.createTransaction(chargeRequestDto.getAmount(), charge.getId(), attendee, event);

            if (!event.getIsOpen()) notificationController.notifyEventCreator(event.getCreator().getEmail());
            return new PaymentResponseDto(true, "Payment successful", charge.getId(), HttpStatus.OK.value());
        } catch (StripeException ex) {
            return new PaymentResponseDto(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @Transactional
    public PaymentResponseDto refundPayment(RefundRequestDto refundRequestDto) {
        try {
            Attendee attendee = attendeeService.findAttendeeById(refundRequestDto.getAttendeeId());
            if (!attendee.getPaymentStatus().equals(PaymentStatus.PRE_REFUND)) {
                return new PaymentResponseDto(false, "Attendee not paid", null, HttpStatus.BAD_REQUEST.value());
            }
            Transaction transaction = transactionService.findTransactionByAttendeeId(refundRequestDto.getAttendeeId());
            Refund refund = stripeService.refundCharge(transaction.getChargeId());

            attendee.setPaymentStatus(PaymentStatus.REFUNDED);
            attendeeService.saveAttendee(attendee);

            transaction.setPaymentStatus(PaymentStatus.REFUNDED);
            transactionService.saveTransaction(transaction);

            return new PaymentResponseDto(true, "Refund successful", refund.getId(), HttpStatus.OK.value());
        } catch (StripeException ex) {
            return new PaymentResponseDto(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        }
    }

}
