package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "creator.id", target = "creatorId")
    EventResponseDto eventToDto(Event event);
}
