package com.actions.executor;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.actions.model.ActionStatus;
import com.actions.utils.objects.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class executes a requested action.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActionExecutor<T> {

    private final List<Action<T>> actions;


    /**
     * Execute action
     *
     * @param actionName action name
     * @param target     target
     * @return ActionResult
     */
    public ActionResult<T> executeAction(final ActionName actionName, T target) {
        Preconditions.assertNotNull(actionName, "action");

        final ActionResult<T> actionResult;
        final Optional<Action<T>> action = actions.stream().filter(findActionByName(actionName)).findFirst();
        try {
            actionResult = action
                    .map(foundAction -> foundAction.execute(target))
                    .orElseGet(() -> ActionResult.error("ACTION-001", "Couldn't find action " + actionName));
        } catch (final RuntimeException e) {
            log.error("Error executing action {}", actionName, e);
            return ActionResult.error("RUNTIME-001", e.getMessage());
        }

        if (ActionStatus.SUCCESS.equals(actionResult.getStatus()) && actionName.hasNextAction()) {
            final ActionName nextAction = actionName.getNextAction();
            return executeAction(nextAction, target);
        }

        return actionResult;
    }


    private Predicate<Action<T>> findActionByName(final ActionName actionName) {
        return action -> action.getName().equals(actionName);
    }

    /**
     * Resumes the action after the given step.
     *
     * @param actionName The current step after that the action shall be resumed.
     * @param target     The target that shall be resumed.
     * @return ActionResult The result of the last step
     */
    public ActionResult<T> resume(final ActionName actionName, final T target) {

        final ActionName nextAction = actionName.getNextAction();
        if (nextAction == null) { // last step
            return ActionResult.success(target);
        }

        log.debug("Continue with next action {}", nextAction);
        return executeAction(nextAction, target);
    }
}
