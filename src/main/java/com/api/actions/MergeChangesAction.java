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
import org.springframework.transaction.annotation.Transactional;

@Component
public class MergeChangesAction implements Action<Story> {

    private final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(MergeChangesAction.class));

    @Override
    public ActionName getName() {
        return ActionType.MERGE_CHANGES;
    }

    @Transactional
    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.MERGED);
        log.info("Merged changes for story {}. Status {}.", story.getName(), story.getStatus());

        return ActionResult.success(story);
    }
}
