package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Transaction;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toTransaction(BigDecimal amount, String chargeId, Attendee attendee, Event event) {
        if ( amount == null && chargeId == null && attendee == null && event == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        if ( attendee != null ) {
            transaction.attendee( attendee );
            transaction.paymentStatus( attendee.getPaymentStatus() );
        }
        transaction.amount( amount );
        transaction.chargeId( chargeId );
        transaction.event( event );

        return transaction.build();
    }
}
