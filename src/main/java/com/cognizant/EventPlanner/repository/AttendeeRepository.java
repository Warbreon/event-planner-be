package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    @Query("SELECT COUNT(a) FROM Attendee a JOIN a.event e " +
            "WHERE e.creator.email = :email AND a.isNewNotification = TRUE")
    int countActiveNotifications(@Param("email") String email);
}
