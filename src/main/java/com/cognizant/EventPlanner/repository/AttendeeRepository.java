package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    @Query("SELECT a FROM Attendee a JOIN FETCH a.event e " +
            "WHERE e.creator.email = :email AND e.isOpen = false AND a.registrationStatus = 'PENDING' AND a.isNewNotification IS NOT NULL " +
            "ORDER BY CASE WHEN a.isNewNotification = TRUE THEN 0 ELSE 1 END, a.registrationTime DESC")
    List<Attendee> findPendingAttendeesWithNotifications(@Param("email") String email);

    @Query("SELECT COUNT(a) FROM Attendee a JOIN a.event e " +
            "WHERE e.creator.email = :email AND a.isNewNotification = TRUE")
    int countActiveNotifications(@Param("email") String email);

    List<Attendee> findAllByEventId(Long eventId);

    @Modifying
    @Query("DELETE FROM Attendee a WHERE a.id IN :attendeesIdsToRemove")
    void removeAllByIdIn(@Param("attendeesIdsToRemove") List<Long> attendeesIdsToRemove);

    @Query("SELECT COUNT(a) FROM Attendee a JOIN a.event e " +
            "WHERE e.creator.email = :email AND a.isNewNotification IS NOT NULL AND a.registrationStatus = 'PENDING'")
    int countAllNotifications(@Param("email") String email);
}
