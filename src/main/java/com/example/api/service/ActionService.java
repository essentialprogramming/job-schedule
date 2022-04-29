package com.example.api.service;

import com.actions.executor.ActionExecutor;
import com.actions.model.ActionName;
import com.example.api.entities.Story;
import com.example.api.mapper.StoryMapper;
import com.example.api.model.StoryInput;
import com.example.api.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActionService {

    private final StoryRepository storyRepository;

    private final ActionExecutor<Story> actionExecutor;

    /**
     * Start action flow and save Story entity to the database
     */
    @Transactional
    public void executeAction(final StoryInput storyInput) {
        final Story story = StoryMapper.inputToEntity(storyInput);

        storyRepository.save(story);

        actionExecutor.executeAction(ActionName.getFirstAction(), story);
    }
}