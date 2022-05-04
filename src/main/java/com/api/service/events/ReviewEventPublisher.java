package com.api.service.events;

import com.actions.model.ActionCompleteEvent;
import com.actions.model.ActionName;
import com.api.actions.ActionType;
import com.api.entities.Story;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishActionReviewEvent(final Story story, final ActionName actionName, final String logInfo) {

        log.info(logInfo);
        ActionCompleteEvent<Story> actionCompleteEvent = new ActionCompleteEvent<>(this, actionName, story);
        applicationEventPublisher.publishEvent(actionCompleteEvent);
    }
}
