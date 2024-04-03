package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AttendeeMapper {

    @Mapping(source = "user", target = "user")
    AttendeeResponseDto attendeeToDto(Attendee attendee);
}
