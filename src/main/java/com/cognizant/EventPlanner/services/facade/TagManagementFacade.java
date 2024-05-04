package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagManagementFacade {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @Cacheable(value = "tags", key = "@userDetailsServiceImpl.getCurrentUserEmail()")
    public List<TagResponseDto> getAllTags() {
        return tagService.findAllTags()
                .stream()
                .map(tagMapper::tagToDto)
                .collect(Collectors.toList());
    }
}
