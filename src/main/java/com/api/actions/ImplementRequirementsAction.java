package com.api.actions;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.entities.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

@Component
@Slf4j
@SuppressWarnings({"FieldCanBeLocal", "SameParameterValue"})
public class ImplementRequirementsAction implements Action<Story> {

    private static final String FILE_PATH = "sample/code.txt";
    private static final String FILE_WITH_CHANGES_PATH = "sample/code_changes.txt";

    @Override
    public ActionName getName() {
        return ActionType.IMPLEMENT_REQUIREMENTS;
    }

    @Override
    public ActionResult<Story> execute(final Story story) {

        if (Status.CHANGES_REQUIRED.equals(story.getStatus())) {

            story.setStatus(Status.IN_PROGRESS);
            log.info("Assignee {} started applying changes on story {}. Status {}.", story.getAssignee(), story.getName(), story.getStatus());
            typeToConsole(FILE_WITH_CHANGES_PATH);
        } else {

            story.setStatus(Status.IN_PROGRESS);
            log.info("Assignee {} started working on story {}. Status {}.", story.getAssignee(), story.getName(), story.getStatus());
            typeToConsole(FILE_PATH);
        }

        return ActionResult.success();
    }

    private static void typeToConsole(String filePath) {

        final String code = readFile(filePath);

        code.chars().forEach(character -> {
            System.out.printf("%c", character);
            parkThread(70);
        });

        System.out.println();
        parkThread(700);
    }

    private static String readFile(final String filePath) {

        try {
            Resource resource = new ClassPathResource(filePath);
            return new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            return "System.out.println(\"I am writing code :)\")";
        }
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
