package com.api.controller;

import com.api.entities.enums.ReviewStatus;
import com.api.output.StoryJSON;
import com.api.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/story")
@RequiredArgsConstructor
@Tag(description = "Story API", name = "Story Services")
public class StoryController {

    private final StoryService storyService;

    @GetMapping
    @Operation(summary = "Get all stories", description = "Get all user stories from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if the operation was successful.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoryJSON.class))))
            }
    )
    public List<StoryJSON> getAll() {
        return storyService.getAll();
    }


    @PostMapping("/review")
    @Operation(summary = "Review pull request", description = "Review pull request for story",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns 200 if the operation was successful."),
                    @ApiResponse(responseCode = "404", description = "Story not found."),
                    @ApiResponse(responseCode = "422", description = "Story not in pull request!")
            }
    )
    public void review(@RequestParam("storyKey") String storyKey,
                       @RequestParam("reviewStatus") ReviewStatus reviewStatus) {

        storyService.reviewPullRequest(storyKey, reviewStatus);
    }
}
