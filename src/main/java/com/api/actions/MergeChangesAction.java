package com.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class MergeChangesAction implements Action<Story> {

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
