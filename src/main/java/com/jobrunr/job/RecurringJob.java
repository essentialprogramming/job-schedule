package com.jobrunr.job;

import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.LockSupport;

@Component
@RequiredArgsConstructor
public class RecurringJob {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(RecurringJob.class));


    @Recurring(id = "my-recurring-job", cron = "*/3 * * * *")
    @Job(name = "A recurring job", retries = 5)
    public void execute() {
        log.info("Recurring job has started..");

        parkThread(12000);
        log.info("Executing job... Progress {}%", 30);

        parkThread(9000);
        log.info("Executing job... Progress {}%", 70);


        parkThread(1500);
        log.info("Almost done... ");

        parkThread(300);
        log.info("Recurring job has finished...");

    }

    private static void parkThread(final Integer ms) {

        final Duration parkPeriod = Duration.of(ms, ChronoUnit.MILLIS);
        final Thread current = Thread.currentThread();

        LockSupport.parkNanos(parkPeriod.toNanos());

        if (Thread.interrupted()) {
            current.interrupt();
        }
    }
}