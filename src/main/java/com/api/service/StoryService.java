package com.api.service;

import com.actions.model.ActionCompleteEvent;
import com.api.actions.ActionType;
import com.api.entities.enums.ReviewStatus;
import com.api.entities.enums.Status;
import com.actions.executor.ActionCompleteEventPublisher;
import com.api.entities.Story;
import com.api.mapper.story.StoryMapper;
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
    private final ActionCompleteEventPublisher actionCompleteEventPublisher;

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

        if (!Status.IN_REVIEW.equals(story.getStatus())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Story not in pull request!");
        }

        switch (reviewStatus) {
            case ACCEPTED: {

                // Review ok, continue with next action after SEND_PULL_REQUEST_EVENT..
                final ActionCompleteEvent<Story> actionCompleteEvent =
                        new ActionCompleteEvent<>(this, ActionType.SEND_PULL_REQUEST_EVENT, story);
                actionCompleteEventPublisher.fire(actionCompleteEvent,
                        "Pull request review finished for story " + story.getName());

                break;
            }
            case REJECTED: {

                story.setStatus(Status.PR_REJECTED);

                // Pull request rejected, continue with next action after SEND_PULL_REQUEST_EVENT..
                final ActionCompleteEvent<Story> actionCompleteEvent =
                        new ActionCompleteEvent<>(this, ActionType.SEND_PULL_REQUEST_EVENT, story);
                actionCompleteEventPublisher.fire(actionCompleteEvent,
                        "Pull request review finished for story " + story.getName());

                break;
            }
            case NEEDS_IMPROVEMENT: {

                story.setStatus(Status.NEEDS_IMPROVEMENT);

                // Review complete(task needs improvement), continue with next action after ASSIGN_STORY..
                final ActionCompleteEvent<Story> actionCompleteEvent =
                        new ActionCompleteEvent<>(this, ActionType.ASSIGN_STORY, story);
                actionCompleteEventPublisher.fire(actionCompleteEvent,
                        "Reimplement story " + story.getName());

                break;
            }
        }
    }
}

