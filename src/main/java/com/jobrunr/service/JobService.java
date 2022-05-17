package com.jobrunr.service;

import com.api.model.StoryInput;
import com.api.workflow.IssueTrackerWorkflow;
import com.jobrunr.enums.CronEnum;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final IssueTrackerWorkflow issueTrackerWorkflow;

    public void enqueue(final StoryInput storyInput) {
        BackgroundJob.enqueue(() -> issueTrackerWorkflow.executeAction(storyInput));
    }

    public void schedule(final StoryInput storyInput, final String dateTime) {
        LocalDateTime dateAndTime = parseTime(dateTime);

        BackgroundJob.schedule(dateAndTime, () -> issueTrackerWorkflow.executeAction(storyInput));
    }

    public void createCronJob(final StoryInput storyInput, final CronEnum cronEnum) {
        switch(cronEnum) {
            case MINUTELY:
                createCronJob(storyInput, Cron.minutely());
                break;
            case HOURLY:
                createCronJob(storyInput, Cron.hourly());
                break;
            case DAILY:
                createCronJob(storyInput, Cron.daily());
                break;
            case YEARLY:
                createCronJob(storyInput, Cron.yearly());
                break;
        }
    }

    private void createCronJob(final StoryInput storyInput, final String cron) {
        BackgroundJob.scheduleRecurrently(String.valueOf(UUID.randomUUID()), cron, () -> issueTrackerWorkflow.executeAction(storyInput));
    }

    private LocalDateTime parseTime(final String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date and time format! Must be of format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
