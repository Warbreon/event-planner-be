package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserAsAttendeeResponseDto userToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserAsAttendeeResponseDto userAsAttendeeResponseDto = new UserAsAttendeeResponseDto();

        userAsAttendeeResponseDto.setId( user.getId() );
        userAsAttendeeResponseDto.setFirstName( user.getFirstName() );
        userAsAttendeeResponseDto.setLastName( user.getLastName() );
        userAsAttendeeResponseDto.setJobTitle( user.getJobTitle() );
        userAsAttendeeResponseDto.setImageUrl( user.getImageUrl() );

        return userAsAttendeeResponseDto;
    }

    @Override
    public UserResponseDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId( user.getId() );
        userResponseDto.setFirstName( user.getFirstName() );
        userResponseDto.setLastName( user.getLastName() );
        userResponseDto.setJobTitle( user.getJobTitle() );
        userResponseDto.setImageUrl( user.getImageUrl() );
        if ( user.getRole() != null ) {
            userResponseDto.setRole( user.getRole().name() );
        }
        userResponseDto.setCity( user.getCity() );
        userResponseDto.setCountry( user.getCountry() );

        return userResponseDto;
    }
}
