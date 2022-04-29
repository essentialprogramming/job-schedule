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
public class MergeChanges implements Action<Story> {

    @Override
    public ActionName getName() {
        return ActionName.MERGE_CHANGES;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.MERGED);
        log.info("Merged changes for story {}. Status {}.", story.getName(), story.getStatus());

        return ActionResult.success();
    }
}
