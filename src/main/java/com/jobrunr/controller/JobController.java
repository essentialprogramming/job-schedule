package com.jobrunr.controller;

import com.api.model.StoryInput;
import com.api.output.ExecutionHistoryJSON;
import com.jobrunr.enums.CronEnum;
import com.jobrunr.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/job")
@RequiredArgsConstructor
@Tag(description = "Job API", name = "Job Scheduler Services")
public class JobController {

    private final JobService jobService;

    @PostMapping("enqueue")
    @Operation(summary = "Enqueue job", description = "Enqueue a given story action job",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was enequeued successfully")
            }
    )
    public String enqueue(@Valid @RequestBody StoryInput storyInput) {
        jobService.enqueue(storyInput);

        return "Action job enqueued successfully!";
    }

    @PostMapping("schedule")
    @Operation(summary = "Schedule job", description = "Schedule a given story action job at a given time",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was scheduled successfully")
            }
    )
    public String schedule(@RequestParam("time") @Schema(example = "16/05/2022 16:00:00") String dateTime,
                           @Valid @RequestBody StoryInput storyInput) {
        jobService.schedule(storyInput, dateTime);

        return "Job scheduled successfully!";
    }

    @PostMapping("schedule/recurrently")
    @Operation(summary = "Schedule job recurrently", description = "Schedule a given story action job recurrently",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if job was scheduled successfully")
            }
    )
    public String scheduleRecurrently(@RequestParam("cron") @Schema(example = "MINUTELY") CronEnum cron,
                           @Valid @RequestBody StoryInput storyInput) {
        jobService.createCronJob(storyInput, cron);

        return "Job scheduled recurrently successfully!";
    }
}
