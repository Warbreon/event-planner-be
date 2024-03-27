package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
}
