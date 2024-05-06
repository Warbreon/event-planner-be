package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    List<Attendee> findAllByEventId(Long eventId);

    @Modifying
    @Query("delete from Attendee a where a.id = :id")
    void removeById(@Param("id") Long id);
}
