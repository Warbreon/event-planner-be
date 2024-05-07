package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.Tag;
import com.cognizant.EventPlanner.repository.EventTagRepository;
import com.cognizant.EventPlanner.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final EventTagRepository eventTagRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional
    public Set<TagResponseDto> addTagsToEvent(Set<Long> tagIds, Event event) {
        return tagIds.stream()
                .map(this::findTagById)
                .map(tag -> {
                    EventTag eventTag = tagMapper.createEventTag(event, tag);
                    saveEventTag(eventTag);
                    return tagMapper.tagToDto(tag);
                })
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addEventTag (EventTag t) {
        eventTagRepository.save(t);
    }

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));
    }

    public List<EventTag> findEventTags(Long eventId){
        return eventTagRepository.findAllByEventId(eventId);
    }

    @Transactional
    public void removeEventTag(Long eventTagId) {
        eventTagRepository.removeById(eventTagId);
    }

    @CacheEvict(value = {"paginatedEvents", "events"}, allEntries = true)
    public void saveEventTag(EventTag eventTag) {
        eventTagRepository.save(eventTag);
    }

    public Set<TagResponseDto> mapEventTags(Set<EventTag> eventTags) {
        return eventTags.stream()
                .map(EventTag::getTag)
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }


    @Transactional
    public void updateEventTags(Event event, Set<Long> newTagIds) {
        List<EventTag> currentTags = findEventTags(event.getId());
        Set<Tag> usedTags = currentTags.stream().map(EventTag::getTag).collect(Collectors.toSet());

        currentTags.forEach(tag -> {
            if (!newTagIds.contains(tag.getTag().getId())) {
                removeEventTag(tag.getId());
            }
        });

        newTagIds.stream()
                .map(this::findTagById)
                .filter(tag -> !usedTags.contains(tag))
                .forEach(tag -> addEventTag(new EventTag(null, event, tag)));
    }
}
