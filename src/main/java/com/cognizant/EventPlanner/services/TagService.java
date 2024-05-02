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

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));
    }

    public void saveEventTag(EventTag eventTag) {
        eventTagRepository.save(eventTag);
    }

    public Set<TagResponseDto> mapEventTags(Set<EventTag> eventTags) {
        return eventTags.stream()
                .map(EventTag::getTag)
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }
}
