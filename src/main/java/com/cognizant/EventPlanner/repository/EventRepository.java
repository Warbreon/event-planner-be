package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByCreatorEmailOrderByEventStartDesc(@Param("email") String email);

    @Query("SELECT e FROM Event e JOIN Attendee a ON e.id = a.event.id" +
            " JOIN User u ON a.user.id = u.id WHERE u.email = :email ORDER BY e.eventStart DESC")
    List<Event> findAllUserIsRegisteredTo(@Param("email") String email);

    @Query("SELECT e FROM Event e JOIN Attendee a ON e.id = a.event.id WHERE a.id = :attendeeId")
    Event findEventByAttendeeId(@Param("attendeeId") Long attendeeId);

    @Query("SELECT e from Event e WHERE e.id = :eventId AND e.creator.id = :creatorId")
    Optional<Event> findEventByIdAndCreatorId(@Param("eventId") Long eventId, @Param("creatorId") Long creatorId);
}
