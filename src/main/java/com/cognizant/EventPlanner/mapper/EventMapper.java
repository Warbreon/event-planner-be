package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "creator.id", target = "creatorId")
    EventResponseDto eventToDto(Event event);
}
