package com.api.mapper.story;

import com.actions.model.ActionResult;
import com.api.entities.Story;
import com.api.output.ExecutionStepJSON;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecutionStepMapper {

    public static List<ExecutionStepJSON> toExecutionStepsJSON(final Map<String, ActionResult<Story>> executions) {
        return executions.entrySet()
                .stream()
                .map(e -> createExecutionStep(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private static ExecutionStepJSON createExecutionStep(final String actionName, final ActionResult<Story> actionResult) {
        return ExecutionStepJSON.builder()
                .actionName(actionName)
                .actionResult(ActionResultMapper.entityToJSON(actionResult))
                .build();
    }
}
