package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttendeeMapper.class, AddressMapper.class, TagMapper.class})
public interface EventMapper {

    @Mapping(source = "address", target = "address")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "attendees", target = "attendees")
    @Mapping(source = "tags", target = "tags")
    EventResponseDto eventToDto(Event event);

    @Mapping(target = "attendees", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Event dtoToEvent(EventRequestDto dto);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "attendees", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "eventStart", ignore = true)
    @Mapping(target = "eventEnd", ignore = true)
    @Mapping(target = "registrationStart", ignore = true)
    @Mapping(target = "registrationEnd", ignore = true)
    Event editEventRequestDtoToEvent(EditEventRequestDto dto);
}
