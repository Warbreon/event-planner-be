package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    @Query("SELECT a.registrationStatus FROM Attendee a JOIN a.user u WHERE a.event.id = :eventId AND u.email = :email")
    Optional<RegistrationStatus> findAttendeeRegistrationStatus(@Param("eventId") Long eventId, @Param("email") String email);

    <S extends Attendee> List<S> saveAll(Iterable<S> attendees);

    Optional<Attendee> findByUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT a FROM Attendee a WHERE a.user.id IN :userIds AND a.event.id = :eventId")
    List<Attendee> findAllByUserIdsAndEventId(@Param("userIds") Set<Long> userIds, @Param("eventId") Long eventId);

    @Query("SELECT COUNT(a) FROM Attendee a JOIN a.event e " +
            "WHERE e.creator.email = :email AND a.isNewNotification = TRUE")
    int countActiveNotifications(@Param("email") String email);
}
