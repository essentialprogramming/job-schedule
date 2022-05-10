package com.api.story;

import com.actions.executor.ActionCompleteEventPublisher;
import com.api.entities.Story;
import com.api.entities.enums.ReviewStatus;
import com.api.entities.enums.Status;
import com.api.repository.StoryRepository;
import com.api.service.StoryService;
import com.util.TestEntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoryServiceTest {

    @InjectMocks
    private StoryService storyService;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private ActionCompleteEventPublisher actionCompleteEventPublisher;

    @Test
    void should_change_status_when_case_is_rejected() {

        //given
        final Story story = TestEntityGenerator.getStory(Status.IN_REVIEW);

        when(storyRepository.findByStoryKey(story.getStoryKey())).thenReturn(Optional.of(story));

        //when
        storyService.reviewPullRequest(story.getStoryKey(), ReviewStatus.REJECTED);

        //then
        assertEquals(Status.PR_REJECTED, story.getStatus());
        verify(actionCompleteEventPublisher).fire(any(), any());
    }

    @Test
    void should_change_status_when_case_is_changes_required() {

        //given
        final Story story = TestEntityGenerator.getStory(Status.IN_REVIEW);

        when(storyRepository.findByStoryKey(story.getStoryKey())).thenReturn(Optional.of(story));

        //when
        storyService.reviewPullRequest(story.getStoryKey(), ReviewStatus.NEEDS_IMPROVEMENT);

        //then
        assertEquals(Status.NEEDS_IMPROVEMENT, story.getStatus());
        verify(actionCompleteEventPublisher).fire(any(), any());
    }

    @Test
    void should_keep_status_when_case_is_accepted() {

        //given
        final Story story = TestEntityGenerator.getStory(Status.IN_REVIEW);

        when(storyRepository.findByStoryKey(story.getStoryKey())).thenReturn(Optional.of(story));

        //when
        storyService.reviewPullRequest(story.getStoryKey(), ReviewStatus.ACCEPTED);

        //then
        assertEquals(Status.IN_REVIEW, story.getStatus());
        verify(actionCompleteEventPublisher).fire(any(), any());
    }

    @Test
    void should_throw_exception_if_story_was_not_found() {

        //given
        final Story story = TestEntityGenerator.getStory(Status.IN_REVIEW);

        when(storyRepository.findByStoryKey(story.getStoryKey())).thenReturn(Optional.empty());

        //when
        Throwable thrownException = assertThrows(
                ResponseStatusException.class,
                () -> storyService.reviewPullRequest(story.getStoryKey(), ReviewStatus.ACCEPTED));

        //then
        assertEquals("404 NOT_FOUND \"Story not found!\"", thrownException.getMessage());
    }

    @Test
    void should_throw_exception_if_story_was_not_in_pull_request_status() {

        //given
        final Story story = TestEntityGenerator.getStory(Status.DONE);

        when(storyRepository.findByStoryKey(story.getStoryKey())).thenReturn(Optional.of(story));

        //when
        Throwable thrownException = assertThrows(
                ResponseStatusException.class,
                () -> storyService.reviewPullRequest(story.getStoryKey(), ReviewStatus.ACCEPTED));

        //then
        assertEquals("422 UNPROCESSABLE_ENTITY \"Story not in pull request!\"", thrownException.getMessage());
    }
}
