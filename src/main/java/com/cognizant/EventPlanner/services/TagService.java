package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.request.EventTagRequestDto;
import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.model.Event;
import com.cognizant.EventPlanner.model.EventTag;
import com.cognizant.EventPlanner.model.Tag;
import com.cognizant.EventPlanner.repository.EventTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final EntityFinderService entityFinderService;
    private final TagMapper tagMapper;
    private final EventTagRepository eventTagRepository;

    public Set<TagResponseDto> getAllTags() {
        return entityFinderService.findAllTags()
                .stream()
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public TagResponseDto addTagToEvent(EventTagRequestDto request) {
        Event event = entityFinderService.findEventById(request.getEventId());
        Tag tag = entityFinderService.findTagById(request.getTagId());

        EventTag eventTag = tagMapper.requestDtoToEventTag(request, event, tag);
        eventTagRepository.save(eventTag);
        return tagMapper.tagToDto(tag);
    }
}
