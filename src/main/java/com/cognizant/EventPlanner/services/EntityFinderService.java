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
import org.springframework.data.jpa.repository.JpaRepository;
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
        return findByIdOrThrow(id, eventRepository, Event.class);
    }

    public List<Event> findEventsByTags(Set<Long> tagIds) {
        return eventRepository.findByTags(tagIds, tagIds.size());
    }

    // User
    public User findUserById(Long id){
        return findByIdOrThrow(id, userRepository, User.class);
    }

    // Tag
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(Long id) {
        return findByIdOrThrow(id, tagRepository, Tag.class);
    }

    // Address
    public Address findAddressById(Long id) {
        return findByIdOrThrow(id, addressRepository, Address.class);
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repository, Class<T> entityClass) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    }
}
