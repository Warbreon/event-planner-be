package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.Tag;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public TagResponseDto tagToDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagResponseDto tagResponseDto = new TagResponseDto();

        tagResponseDto.setId( tag.getId() );
        tagResponseDto.setName( tag.getName() );

        return tagResponseDto;
    }

    @Override
    public EventTag createEventTag(Event event, Tag tag) {
        if ( event == null && tag == null ) {
            return null;
        }

        EventTag.EventTagBuilder eventTag = EventTag.builder();

        eventTag.event( event );
        eventTag.tag( tag );

        return eventTag.build();
    }
}
