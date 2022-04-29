package com.example.api.mapper;

import com.example.api.entities.Story;
import com.example.api.model.StoryInput;
import com.example.api.output.StoryJSON;

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
