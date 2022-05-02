package com.api.service;

import com.api.mapper.StoryMapper;
import com.api.output.StoryJSON;
import com.api.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    /**
     * Looks up and retrieves all user stories stored in the database.
     * @return List of all found stories.
     */
    public List<StoryJSON> getAll() {
        return storyRepository.findAll()
                .stream()
                .map(StoryMapper::entityToJSON)
                .collect(Collectors.toList());
    }
}
