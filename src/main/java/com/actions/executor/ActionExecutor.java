package com.actions.executor;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.actions.model.ActionStatus;
import com.actions.utils.model.Failure;
import com.actions.utils.objects.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class can be used to execute a single action.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActionExecutor<T> {

    private final List<Action<T>> actions;


    /**
     * execute action
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
            return ActionResult.<T>builder()
                    .status(ActionStatus.FAILED)
                    .failure(Failure.builder().description(e.getMessage()).build())
                    .build();
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
     * Resumes the order submit after the given step.
     *
     * @param actionName The current step after that the order submit shall be resumed.
     * @param target     The target that shall be resumed.
     * @return ActionResult the result of the last step
     */
    public ActionResult<T> resume(final ActionName actionName, final T target) {

        final ActionName nextAction = actionName.getNextAction();
        if (nextAction == null) { // last step
            return ActionResult.<T>builder().status(ActionStatus.SUCCESS).build();
        }

        log.debug("Continue with next action {}", nextAction);
        return executeAction(nextAction, target);
    }
}
