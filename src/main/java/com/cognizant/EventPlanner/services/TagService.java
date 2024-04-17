package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.mapper.TagMapper;
import com.cognizant.EventPlanner.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public Set<TagResponseDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::tagToDto)
                .collect(Collectors.toSet());
    }
}
