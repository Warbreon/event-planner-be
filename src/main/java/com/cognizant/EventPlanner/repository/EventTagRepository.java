package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
    List<EventTag> findAllByEventId(Long eventId);

    @Modifying
    @Query("DELETE FROM EventTag t WHERE t.id IN :eventTagsToRemove")
    void removeAllByIdIn(@Param("eventTagsToRemove") List<Long> eventTagsToRemove);
}
