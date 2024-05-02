package com.cognizant.EventPlanner.specification;

import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Set;

public class EventSpecifications {

    public static Specification<Event> hasTags(Set<Long> tagIds) {
        return (root, query, cb) -> {
            if (tagIds.isEmpty()) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Event> subEvent = subquery.from(Event.class);
            Join<Event, EventTag> subTags = subEvent.join("tags");

            subquery.select(cb.literal(1L))
                    .where(cb.equal(subEvent, root), subTags.get("tag").get("id").in(tagIds))
                    .groupBy(subEvent)
                    .having(cb.equal(cb.countDistinct(subTags.get("tag").get("id")), tagIds.size()));
            return cb.exists(subquery);
        };
    }

    public static Specification<Event> withinDays(int days) {
        LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = now.plusDays(days).minusNanos(1);
        return (root, query, cb) -> cb.between(root.get("eventStart"), now, end);
    }

    public static Specification<Event> byCity(String city) {
        return (root, query, cb) -> {
            if (city == null) {
                return cb.conjunction();
            } else if (city.equalsIgnoreCase("Online")) {
                return cb.isNull(root.get("address"));
            } else {
                return cb.equal(root.join("address").get("city"), city);
            }
        };
    }
}
