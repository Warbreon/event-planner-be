package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.dto.response.UserResponseDto;
import com.cognizant.EventPlanner.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserAsAttendeeResponseDto userToDto(User user);

    UserResponseDto userToUserDto(User user);
}
