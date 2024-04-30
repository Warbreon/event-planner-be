package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.UserAsAttendeeResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.UserMapper;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserAsAttendeeResponseDto> getAllUsers() {
        return findAllUsers().stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(User.class, email));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUserRegistered(Event event, String userEmail) {
        return event.getAttendees()
                .stream()
                .anyMatch(attendee -> attendee.getUser().getEmail().equals(userEmail));
    }

    private UserAsAttendeeResponseDto convertUserToDto(User user) {
        return userMapper.userToDto(user);
    }
}
