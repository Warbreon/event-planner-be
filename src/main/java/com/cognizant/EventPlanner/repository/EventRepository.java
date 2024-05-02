package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT DISTINCT e FROM Event e JOIN e.tags t WHERE t.tag.id IN :tagIds " +
            "GROUP BY e HAVING COUNT(DISTINCT t.tag.id) = :countOfTags ORDER BY e.eventStart ASC")
    List<Event> findByTags(@Param("tagIds") Set<Long> tagIds, @Param("countOfTags") long countOfTags);

    List<Event> findAllByOrderByEventStartAsc();

    List<Event> findAllByCreatorEmail(@Param("email") String email);

    @Query("SELECT e FROM Event e JOIN Attendee a ON e.id = a.event.id" +
            " JOIN User u ON a.user.id = u.id WHERE u.email = :email")
    List<Event> findAllUserIsRegisteredTo(@Param("email") String email);
}
