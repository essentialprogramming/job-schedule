package com.util;

import com.api.entities.Story;
import com.api.entities.enums.Status;
import com.api.model.StoryInput;

public class TestEntityGenerator {

    public static StoryInput getStoryInput() {
        return StoryInput.builder()
                .name("my-story")
                .assignee("my-assignee")
                .build();
    }


    public static Story getStory(Status status) {
        return Story.builder()
                .storyKey("story-key")
                .assignee("story-assignee")
                .name("story-name")
                .status(status)
                .build();
    }
}
