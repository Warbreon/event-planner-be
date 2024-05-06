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
    @Query("delete from EventTag t where t.id = :id")
    void removeById(@Param("id") Long id);
}
