package com.actions.executor;


import com.actions.model.ActionCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionListener<T> {

    private final ActionExecutor<T> executor;

    @EventListener
    public void onActionComplete(final ActionCompleteEvent<T> event) {
        log.info("Resume action {}", event.getActionName());
        executor.resume(event.getActionName(), event.getTarget());
    }
}
