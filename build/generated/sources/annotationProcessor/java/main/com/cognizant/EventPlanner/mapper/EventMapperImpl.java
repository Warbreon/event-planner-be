package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.request.EditEventRequestDto;
import com.cognizant.EventPlanner.dto.request.EventRequestDto;
import com.cognizant.EventPlanner.dto.response.AttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.EventResponseDto;
import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.model.Attendee;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.User;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Autowired
    private AttendeeMapper attendeeMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public EventResponseDto eventToDto(Event event) {
        if ( event == null ) {
            return null;
        }

        EventResponseDto eventResponseDto = new EventResponseDto();

        eventResponseDto.setAddress( addressMapper.addressToDto( event.getAddress() ) );
        eventResponseDto.setCreatorId( eventCreatorId( event ) );
        eventResponseDto.setAttendees( attendeeSetToAttendeeResponseDtoSet( event.getAttendees() ) );
        eventResponseDto.setTags( eventTagSetToTagResponseDtoSet( event.getTags() ) );
        eventResponseDto.setName( event.getName() );
        eventResponseDto.setDescription( event.getDescription() );
        eventResponseDto.setIsOpen( event.getIsOpen() );
        eventResponseDto.setEventStart( event.getEventStart() );
        eventResponseDto.setEventEnd( event.getEventEnd() );
        eventResponseDto.setRegistrationStart( event.getRegistrationStart() );
        eventResponseDto.setRegistrationEnd( event.getRegistrationEnd() );
        String[] agenda = event.getAgenda();
        if ( agenda != null ) {
            eventResponseDto.setAgenda( Arrays.copyOf( agenda, agenda.length ) );
        }
        eventResponseDto.setPrice( event.getPrice() );
        eventResponseDto.setTickets( event.getTickets() );
        eventResponseDto.setCurrency( event.getCurrency() );
        eventResponseDto.setInviteUrl( event.getInviteUrl() );
        eventResponseDto.setId( event.getId() );
        eventResponseDto.setImageUrl( event.getImageUrl() );
        eventResponseDto.setCardImageUrl( event.getCardImageUrl() );
        eventResponseDto.setCreatedDate( event.getCreatedDate() );
        eventResponseDto.setIsCancelled( event.getIsCancelled() );

        return eventResponseDto;
    }

    @Override
    public Event dtoToEvent(EventRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.name( dto.getName() );
        event.description( dto.getDescription() );
        event.isOpen( dto.getIsOpen() );
        event.eventStart( dto.getEventStart() );
        event.eventEnd( dto.getEventEnd() );
        event.registrationStart( dto.getRegistrationStart() );
        event.registrationEnd( dto.getRegistrationEnd() );
        String[] agenda = dto.getAgenda();
        if ( agenda != null ) {
            event.agenda( Arrays.copyOf( agenda, agenda.length ) );
        }
        event.tickets( dto.getTickets() );
        event.price( dto.getPrice() );
        event.inviteUrl( dto.getInviteUrl() );
        event.currency( dto.getCurrency() );

        return event.build();
    }

    @Override
    public Event editEventRequestDtoToEvent(EditEventRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.name( dto.getName() );
        event.description( dto.getDescription() );
        event.isOpen( dto.getIsOpen() );
        String[] agenda = dto.getAgenda();
        if ( agenda != null ) {
            event.agenda( Arrays.copyOf( agenda, agenda.length ) );
        }
        event.tickets( dto.getTickets() );
        event.price( dto.getPrice() );
        event.inviteUrl( dto.getInviteUrl() );
        event.currency( dto.getCurrency() );

        return event.build();
    }

    private Long eventCreatorId(Event event) {
        if ( event == null ) {
            return null;
        }
        User creator = event.getCreator();
        if ( creator == null ) {
            return null;
        }
        Long id = creator.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<AttendeeResponseDto> attendeeSetToAttendeeResponseDtoSet(Set<Attendee> set) {
        if ( set == null ) {
            return null;
        }

        Set<AttendeeResponseDto> set1 = new LinkedHashSet<AttendeeResponseDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Attendee attendee : set ) {
            set1.add( attendeeMapper.attendeeToDto( attendee ) );
        }

        return set1;
    }

    protected TagResponseDto eventTagToTagResponseDto(EventTag eventTag) {
        if ( eventTag == null ) {
            return null;
        }

        TagResponseDto tagResponseDto = new TagResponseDto();

        tagResponseDto.setId( eventTag.getId() );

        return tagResponseDto;
    }

    protected Set<TagResponseDto> eventTagSetToTagResponseDtoSet(Set<EventTag> set) {
        if ( set == null ) {
            return null;
        }

        Set<TagResponseDto> set1 = new LinkedHashSet<TagResponseDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( EventTag eventTag : set ) {
            set1.add( eventTagToTagResponseDto( eventTag ) );
        }

        return set1;
    }
}
