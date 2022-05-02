package com.api.mapper;

import com.api.output.StoryJSON;
import com.api.entities.Story;
import com.api.model.StoryInput;

public class StoryMapper {

    public static Story inputToEntity(final StoryInput storyInput) {
        return Story.builder()
                .name(storyInput.getName())
                .assignee(storyInput.getAssignee())
                .build();
    }

    public static StoryJSON entityToJSON(final Story story) {
        return StoryJSON.builder()
                .name(story.getName())
                .status(story.getStatus().getValue())
                .assignee(story.getAssignee())
                .build();
    }
}
