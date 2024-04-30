package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.request.AttendeeRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AttendeeMapper {

    @Mapping(source = "user", target = "user")
    AttendeeResponseDto attendeeToDto(Attendee attendee);

    @Mapping(expression = "java(java.time.LocalDateTime.now())", target = "registrationTime")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isNewNotification", ignore = true)
    @Mapping(target = "registrationStatus", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    Attendee requestDtoToAttendee(AttendeeRequestDto requestDto, Event event, User user);
}
