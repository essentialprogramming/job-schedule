package com.api.mapper.story;

import com.actions.model.ActionResult;
import com.api.output.ActionResultJSON;

public class ActionResultMapper {

    public static <T> ActionResultJSON entityToJSON(final ActionResult<T> actionResult) {
        return ActionResultJSON.builder()
                .failure(actionResult.getFailure())
                .status(actionResult.getStatus())
                .build();
    }
}