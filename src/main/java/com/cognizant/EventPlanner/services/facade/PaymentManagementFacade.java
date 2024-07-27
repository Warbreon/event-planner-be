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

import java.math.BigDecimal;
import java.util.Objects;
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

            User user = getCurrentUser();
            Event event = eventService.findEventById(chargeRequestDto.getEventId());
            Optional<Attendee> existingAttendee = attendeeService.findAttendeeByUserAndEvent(user.getId(), event.getId());

            Optional<PaymentResponseDto> validationResponse = validateExistingAttendee(existingAttendee);
            if (validationResponse.isPresent()) return validationResponse.get();

            registerAttendee(user, event, chargeRequestDto.getAmount(), charge.getId());
            if (!event.getIsOpen()) {
                notificationController.notifyEventCreator(event.getCreator().getEmail());
            }
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

            updateAttendeeAndTransactionStatus(attendee, transaction);

            return new PaymentResponseDto(true, "Refund successful", refund.getId(), HttpStatus.OK.value());
        } catch (StripeException ex) {
            return new PaymentResponseDto(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        }
    }

    private User getCurrentUser() {
        String userEmail = userDetailsService.getCurrentUserEmail();
        return userService.findUserByEmail(userEmail);
    }

    private Optional<PaymentResponseDto> validateExistingAttendee(Optional<Attendee> existingAttendee) {
        if (existingAttendee.isPresent()) {
            if (existingAttendee.get().getPaymentStatus().equals(PaymentStatus.PAID)) {
                return Optional.of(new PaymentResponseDto(false, "Attendee already registered", null, HttpStatus.BAD_REQUEST.value()));
            } else if (existingAttendee.get().getRegistrationStatus().equals(RegistrationStatus.REJECTED)) {
                return Optional.of(new PaymentResponseDto(false, "Attendee rejected", null, HttpStatus.BAD_REQUEST.value()));
            }
        }
        return Optional.empty();
    }

    private void registerAttendee(User user, Event event, BigDecimal amount, String chargeId) {
        boolean isUserCreator = isUserCreator(user, event);
        AttendeeRequestDto attendeeRequest = new AttendeeRequestDto();
        attendeeRequest.setUserId(user.getId());
        attendeeRequest.setEventId(event.getId());

        Attendee attendeeToRegister = registrationService.createAttendee(attendeeRequest, user, event, isUserCreator);
        registrationService.updateAttendeeStatuses(isUserCreator, attendeeToRegister, event, event.getIsOpen() ? RegistrationStatus.ACCEPTED : RegistrationStatus.PENDING, PaymentStatus.PAID);
        Attendee attendee = attendeeService.saveAttendee(attendeeToRegister);

        transactionService.createTransaction(amount, chargeId, attendee, event);
    }

    private void updateAttendeeAndTransactionStatus(Attendee attendee, Transaction transaction) {
        attendee.setPaymentStatus(PaymentStatus.REFUNDED);
        attendeeService.saveAttendee(attendee);

        transaction.setPaymentStatus(PaymentStatus.REFUNDED);
        transactionService.saveTransaction(transaction);
    }

    private boolean isUserCreator(User user, Event event) {
        return Objects.equals(user.getEmail(), event.getCreator().getEmail());
    }
}
