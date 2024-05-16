package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.AttendeeNotificationResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface NotificationMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "event.eventStart", target = "eventStart")
    @Mapping(source = "user", target = "user")
    AttendeeNotificationResponseDto attendeeNotificationToDto(Attendee attendee);
}
