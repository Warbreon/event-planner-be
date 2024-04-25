package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.TagResponseDto;
import com.cognizant.EventPlanner.services.facade.TagManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagManagementFacade tagManagementFacade;

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        List<TagResponseDto> tags = tagManagementFacade.getAllTags();
        return ResponseEntity.ok(tags);
    }
}
