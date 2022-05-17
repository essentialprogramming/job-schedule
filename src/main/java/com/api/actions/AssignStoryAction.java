package com.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class AssignStoryAction implements Action<Story> {

    private final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(AssignStoryAction.class));

    @Override
    public ActionName getName() {
        return ActionType.ASSIGN_STORY;
    }

    @Override
    public ActionResult<Story> execute(final Story story) {

        story.setStatus(Status.CREATED);
        log.info("Story {} assigned to {}. Status {}.", story.getName(), story.getAssignee(), story.getStatus());

        return ActionResult.success(story);

    }
}
