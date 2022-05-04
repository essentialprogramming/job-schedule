package com.api.workflow;

import com.actions.executor.ActionExecutor;
import com.actions.model.Workflow;
import com.api.actions.ActionType;
import com.api.entities.Story;
import com.api.mapper.story.ExecutionStepMapper;
import com.api.mapper.story.StoryMapper;
import com.api.model.StoryInput;
import com.api.output.ExecutionHistoryJSON;
import com.api.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This class defines the issue/story tracking workflow.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IssueTrackerWorkflow implements Workflow<Story> {

    private final StoryRepository storyRepository;
    private final ActionExecutor<Story> actionExecutor;

    /**
     * Start action flow and save Story entity to the database
     */
    @Transactional
    public ExecutionHistoryJSON executeAction(final StoryInput storyInput) {
        final Story story = StoryMapper.inputToEntity(storyInput);
        storyRepository.save(story);

        return execute(story);

    }

    @Override
    public ExecutionHistoryJSON execute(final Story story) {
        return ExecutionHistoryJSON.builder()
                .storyKey(story.getStoryKey())
                .executionSteps(ExecutionStepMapper.toExecutionStepsJSON(actionExecutor.executeAction(ActionType.ASSIGN_STORY, story)))
                .build();
    }

    @Override
    public List<String> getActionNames() {
        return Stream.of(ActionType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}