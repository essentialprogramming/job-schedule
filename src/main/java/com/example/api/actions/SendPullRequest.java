package com.example.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.example.api.entities.Story;
import com.example.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.LockSupport;

@Component
@Slf4j
public class SendPullRequest implements Action<Story> {

    @Override
    public ActionName getName() {
        return ActionName.SEND_PULL_REQUEST_EVENT;
    }

    @Override
    public ActionResult<Story> execute(Story story) {

        log.info("Sending pull request for story {}. Status {}.", story.getName(), story.getStatus());
        story.setStatus(Status.PULL_REQUEST);
        // thread sleep
        log.info("Waiting for pull request review for story {}. Status {}.", story.getName(), story.getStatus());

        park(() -> story);

        log.info("Pull request review received for story {}. Status {}", story.getName(), story.getStatus());

        return ActionResult.success();
    }

    private static <T> void park(Callable execution) {
        final Duration parkPeriod = Duration.of(30, ChronoUnit.SECONDS);

        Thread current = Thread.currentThread();

        log.info("Execution parked for {} ms", parkPeriod.toMillis());
        LockSupport.parkNanos(execution, parkPeriod.toNanos());

        if (Thread.interrupted()) {
            current.interrupt();
        }
    }
}
