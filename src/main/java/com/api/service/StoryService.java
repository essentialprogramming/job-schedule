package com.api.service;

import com.actions.executor.ActionEventPublisher;
import com.api.entities.Story;
import com.api.mapper.StoryMapper;
import com.api.output.StoryJSON;
import com.api.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final ActionEventPublisher actionEventPublisher;

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

    public void reviewPullRequest(String storyKey) {

        Story story = storyRepository.findByStoryKey(storyKey)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Story not found!"));

        actionEventPublisher.publishActionReviewEvent(story);
    }
}
