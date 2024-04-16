package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.sun.jdi.request.EventRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttendeeMapper.class, AddressMapper.class})
public interface EventMapper {

    @Mapping(source = "address", target = "address")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "attendees", target = "attendees")
    EventResponseDto eventToDto(Event event);

    @Mapping(target = "attendees", ignore = true)
    Event dtoToEvent(EventRequestDto dto);
}
