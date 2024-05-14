package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponseDto tagToDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    EventTag createEventTag(Event event, Tag tag);
}
