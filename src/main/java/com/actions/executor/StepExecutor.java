package com.actions.executor;

import com.actions.model.Action;
import com.actions.model.ActionResult;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Executes the actions/workflow steps and safes the execution state (results).
 */
@NoArgsConstructor
public class StepExecutor<T> {

    private final Map<String, ActionResult<T>> executionHistory = new LinkedHashMap<>();

    /**
     * @return the result of all executed actions/workflow steps
     */
    public Map<String, ActionResult<T>> getExecutionHistory() {
        return executionHistory;
    }

    public ActionResult<T> execute(final Action<T> action, final T target) {
        final ActionResult<T> actionResult;

        actionResult = action.execute(target);
        executionHistory.put(action.getName().toString(), actionResult);

        return actionResult;
    }

}
