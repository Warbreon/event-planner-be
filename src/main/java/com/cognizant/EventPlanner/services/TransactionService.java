package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.TransactionMapper;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Transaction;
import com.cognizant.EventPlanner.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public void createTransaction(BigDecimal amount, String chargeId, Attendee attendee, Event event) {
        Transaction transaction = transactionMapper.toTransaction(amount, chargeId, attendee, event);
        saveTransaction(transaction);
    }

    public Transaction findTransactionByAttendeeId(Long attendeeId) {
        return transactionRepository.findByAttendeeId(attendeeId)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, attendeeId));
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
