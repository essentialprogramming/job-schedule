package com.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImplementStory implements Action<Story> {


    @Override
    public ActionName getName() {
        return ActionType.IMPLEMENT_REQUIREMENTS;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.IN_PROGRESS);
        log.info("Assignee {} started working on story {}. Status {}.", story.getAssignee(), story.getName(), story.getStatus());

        return ActionResult.success();
    }
}
