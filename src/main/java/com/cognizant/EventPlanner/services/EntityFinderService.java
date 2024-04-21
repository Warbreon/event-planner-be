package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.Tag;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.AddressRepository;
import com.cognizant.EventPlanner.repository.EventRepository;
import com.cognizant.EventPlanner.repository.TagRepository;
import com.cognizant.EventPlanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EntityFinderService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final AddressRepository addressRepository;

    // Event
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, id));
    }

    public List<Event> findEventsByTags(Set<Long> tagIds) {
        return eventRepository.findByTags(tagIds, tagIds.size());
    }

    // User
    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    // Tag
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));
    }

    // Address
    public Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Address.class, id));
    }
}
