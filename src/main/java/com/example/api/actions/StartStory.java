package com.example.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.example.api.entities.Story;
import com.example.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class StartStory implements Action<Story> {

    @Override
    public ActionName getName() {
        return ActionName.START_WORKING_ON_STORY;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.CREATED);
        log.info("Story {} assigned to {}. Status {}.", story.getName(), story.getAssignee(), story.getStatus());

        return ActionResult.success();
    }
}
