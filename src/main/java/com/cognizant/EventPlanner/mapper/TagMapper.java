package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponseDto tagToDto(Tag tag);
}
