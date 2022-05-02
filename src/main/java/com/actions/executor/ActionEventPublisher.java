package com.actions.executor;

import com.actions.model.ActionCompleteEvent;
import com.api.actions.ActionType;
import com.api.entities.Story;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActionEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishActionReviewEvent(Story story) {

        log.info("Pull request review received for story {}. Status {}", story.getName(), story.getStatus());
        ActionCompleteEvent<Story> actionCompleteEvent = new ActionCompleteEvent<>(story, ActionType.SEND_PULL_REQUEST_EVENT, story);
        applicationEventPublisher.publishEvent(actionCompleteEvent);
    }
}
