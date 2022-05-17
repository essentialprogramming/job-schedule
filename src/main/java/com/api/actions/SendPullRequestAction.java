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
public class SendPullRequestAction implements Action<Story> {

    private final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(SendPullRequestAction.class));

    @Override
    public ActionName getName() {
        return ActionType.SEND_PULL_REQUEST_EVENT;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.IN_REVIEW);
        log.info("Pull request for story {} has been sent. Status {}.", story.getName(), story.getStatus());
        log.info("Waiting for pull request review...");

        return ActionResult.waiting();
    }
}
