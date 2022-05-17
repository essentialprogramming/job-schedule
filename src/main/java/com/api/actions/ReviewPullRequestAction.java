package com.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.entities.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReviewPullRequestAction implements Action<Story> {

    private final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(ReviewPullRequestAction.class));

    @Override
    public ActionName getName() {
        return ActionType.REVIEW_PULL_REQUEST_EVENT;
    }

    @Override
    public ActionResult<Story> execute(final Story story) {

        if(Status.PR_REJECTED.equals(story.getStatus())) {

            log.info("Pull request for story {} was rejected! Action flow stopped.", story.getName());
            return ActionResult.error("PR-001", "Pull request rejected!");
        }

        log.info("Pull request for story {} was accepted! Merging story...", story.getName());
        return ActionResult.success(story);
    }
}
