package com.actions.executor;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.actions.utils.objects.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class executes a requested action.
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class ActionExecutor<T> {

    private final List<Action<T>> actions;

    /**
     * Execute an action
     *
     * @param actionName       action name
     * @param target           target
     * @return ActionResult[]  The results of all executed actions/workflow steps
     */
    public Map<String, ActionResult<T>> executeAction(final ActionName actionName, final T target) {
        Preconditions.assertNotNull(actionName, "actionName");
        Preconditions.assertNotNull(target, "target");

        final StepExecutor<T> stepExecutor = new StepExecutor<>();
        return executeAction(actionName, target, stepExecutor);
    }

    private Map<String, ActionResult<T>> executeAction(final ActionName actionName, final T target, final StepExecutor<T> stepExecutor) {

        final Optional<Action<T>> action = actions.stream().filter(findActionByName(actionName)).findFirst();
        final ActionResult<T> actionResult;
        try {
            actionResult = action
                    .map(foundAction -> stepExecutor.execute(foundAction, target))
                    .orElseGet(() -> ActionResult.error("ACTION-001", "Couldn't find action " + actionName));
        } catch (final RuntimeException e) {
            log.error("Error executing action {}", actionName, e);
            return ImmutableMap.of(actionName.toString(), ActionResult.error("RUNTIME-001", e.getMessage()));
        }

        if (actionResult.isSuccess() && actionName.hasNextAction()) {
            final ActionName nextAction = actionName.getNextAction();
            return executeAction(nextAction, target, stepExecutor);
        }

        return stepExecutor.getExecutionHistory();
    }

    /**
     * Resumes the action after the given step.
     *
     * @param actionName      The current step after that the action shall be resumed.
     * @param target          The target that shall be resumed.
     * @return ActionResult[] The results of all executed actions/workflow steps
     */
    public Map<String, ActionResult<T>> resume(final ActionName actionName, final T target) {
        final StepExecutor<T> stepExecutor = new StepExecutor<>();
        final ActionName nextAction = actionName.getNextAction();
        if (nextAction == null) { // last step
            return ImmutableMap.of(actionName.toString(), ActionResult.success(target));
        }

        log.debug("Continue with next action {}", nextAction);
        return executeAction(nextAction, target, stepExecutor);
    }


    private Predicate<Action<T>> findActionByName(final ActionName actionName) {
        return action -> action.getName().equals(actionName);
    }


}
