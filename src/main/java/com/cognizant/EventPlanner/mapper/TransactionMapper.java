package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attendee", source = "attendee")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "chargeId", source = "chargeId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "paymentStatus", source = "attendee.paymentStatus")
    Transaction toTransaction(BigDecimal amount, String chargeId, Attendee attendee, Event event);
}