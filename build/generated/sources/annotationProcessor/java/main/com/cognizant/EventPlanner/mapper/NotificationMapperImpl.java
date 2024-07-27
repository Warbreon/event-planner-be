package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.AttendeeNotificationResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public AttendeeNotificationResponseDto attendeeNotificationToDto(Attendee attendee) {
        if ( attendee == null ) {
            return null;
        }

        AttendeeNotificationResponseDto attendeeNotificationResponseDto = new AttendeeNotificationResponseDto();

        attendeeNotificationResponseDto.setEventId( attendeeEventId( attendee ) );
        attendeeNotificationResponseDto.setEventName( attendeeEventName( attendee ) );
        attendeeNotificationResponseDto.setEventStart( attendeeEventEventStart( attendee ) );
        attendeeNotificationResponseDto.setUser( userMapper.userToDto( attendee.getUser() ) );
        attendeeNotificationResponseDto.setId( attendee.getId() );
        attendeeNotificationResponseDto.setRegistrationStatus( attendee.getRegistrationStatus() );
        attendeeNotificationResponseDto.setPaymentStatus( attendee.getPaymentStatus() );
        attendeeNotificationResponseDto.setRegistrationTime( attendee.getRegistrationTime() );
        attendeeNotificationResponseDto.setIsNewNotification( attendee.getIsNewNotification() );

        return attendeeNotificationResponseDto;
    }

    private Long attendeeEventId(Attendee attendee) {
        if ( attendee == null ) {
            return null;
        }
        Event event = attendee.getEvent();
        if ( event == null ) {
            return null;
        }
        Long id = event.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String attendeeEventName(Attendee attendee) {
        if ( attendee == null ) {
            return null;
        }
        Event event = attendee.getEvent();
        if ( event == null ) {
            return null;
        }
        String name = event.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private LocalDateTime attendeeEventEventStart(Attendee attendee) {
        if ( attendee == null ) {
            return null;
        }
        Event event = attendee.getEvent();
        if ( event == null ) {
            return null;
        }
        LocalDateTime eventStart = event.getEventStart();
        if ( eventStart == null ) {
            return null;
        }
        return eventStart;
    }
}
