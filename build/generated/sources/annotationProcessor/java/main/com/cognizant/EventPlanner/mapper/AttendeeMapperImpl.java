package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class AttendeeMapperImpl implements AttendeeMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public AttendeeResponseDto attendeeToDto(Attendee attendee) {
        if ( attendee == null ) {
            return null;
        }

        AttendeeResponseDto attendeeResponseDto = new AttendeeResponseDto();

        attendeeResponseDto.setUser( userMapper.userToDto( attendee.getUser() ) );
        attendeeResponseDto.setId( attendee.getId() );
        attendeeResponseDto.setRegistrationStatus( attendee.getRegistrationStatus() );
        attendeeResponseDto.setPaymentStatus( attendee.getPaymentStatus() );
        attendeeResponseDto.setRegistrationTime( attendee.getRegistrationTime() );
        attendeeResponseDto.setIsNewNotification( attendee.getIsNewNotification() );

        return attendeeResponseDto;
    }

    @Override
    public Attendee requestDtoToAttendee(AttendeeRequestDto requestDto, Event event, User user) {
        if ( requestDto == null && event == null && user == null ) {
            return null;
        }

        Attendee.AttendeeBuilder attendee = Attendee.builder();

        attendee.event( event );
        attendee.user( user );
        attendee.registrationTime( java.time.LocalDateTime.now() );

        return attendee.build();
    }
}
