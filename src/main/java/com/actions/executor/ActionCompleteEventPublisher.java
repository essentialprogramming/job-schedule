package com.actions.executor;

import com.actions.model.ActionCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActionCompleteEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public <T> void fire(final ActionCompleteEvent<T> actionCompleteEvent, final String info) {

        log.info(info);
        applicationEventPublisher.publishEvent(actionCompleteEvent);
    }
}
