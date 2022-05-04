package com.api.service;

import com.api.actions.ActionType;
import com.api.entities.enums.ReviewStatus;
import com.api.entities.enums.Status;
import com.api.service.events.ReviewEventPublisher;
import com.api.entities.Story;
import com.api.mapper.StoryMapper;
import com.api.output.StoryJSON;
import com.api.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final ReviewEventPublisher reviewEventPublisher;

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

    @Transactional
    public void reviewPullRequest(String storyKey, ReviewStatus reviewStatus) {

        Story story = storyRepository.findByStoryKey(storyKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found!"));

        if (!Status.PULL_REQUEST.equals(story.getStatus())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Story not in pull request!");
        }

        if (ReviewStatus.REJECTED.equals(reviewStatus)) {

            story.setStatus(Status.PR_REJECTED);
            reviewEventPublisher.publishActionReviewEvent(story, ActionType.SEND_PULL_REQUEST_EVENT,
                    "Pull request review finished for story " + story.getName());

        } else if(ReviewStatus.CHANGES_REQUIRED.equals(reviewStatus)) {

            story.setStatus(Status.CHANGES_REQUIRED);
            reviewEventPublisher.publishActionReviewEvent(story, ActionType.ASSIGN_STORY,
                    "Reimplement story " + story.getName());

        } else if (ReviewStatus.ACCEPTED.equals(reviewStatus)) {

            reviewEventPublisher.publishActionReviewEvent(story, ActionType.SEND_PULL_REQUEST_EVENT,
                    "Pull request review finished for story " + story.getName());
        }
    }
}

