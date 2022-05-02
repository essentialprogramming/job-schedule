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
public class SendStoryCompleteNotification implements Action<Story> {

    @Override
    public ActionName getName() {
        return ActionType.SEND_STORY_COMPLETE_NOTIFICATION;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        story.setStatus(Status.DONE);
        log.info("Story {} completed. Status {}.", story.getName(), story.getStatus());

        return ActionResult.success();
    }
}
