package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttendeeMapper.class, AddressMapper.class, TagMapper.class})
public interface EventMapper {

    @Mapping(source = "open", target = "open")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "attendees", target = "attendees")
    @Mapping(source = "tags", target = "tags")
    EventResponseDto eventToDto(Event event);
}
