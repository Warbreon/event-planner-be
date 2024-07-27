package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByAttendeeId(Long attendeeId);

    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.attendee.id IN :attendeesIdsToRemove")
    void removeAllByIdIn(@Param("attendeesIdsToRemove") List<Long> attendeesIdsToRemove);
}
