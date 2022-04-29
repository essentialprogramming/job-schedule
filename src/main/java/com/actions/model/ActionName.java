package com.actions.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * This enum defines the actions to be used in the action executor.
 */
public enum ActionName {

    START_WORKING_ON_STORY(1),
    IMPLEMENT_REQUIREMENTS(2),
    SEND_PULL_REQUEST_EVENT(3),
    MERGE_CHANGES(4),
    SEND_STORY_COMPLETE_NOTIFICATION(5);

    private final Integer executionSequence;

    ActionName(final Integer executionSequence) {
        this.executionSequence = executionSequence;
    }

    public boolean hasNextAction() {
        return getNextAction() != null;
    }

    /**
     * Gets next action.
     *
     * @return Next action or null if the current is already the last in the execution sequence.
     */
    public ActionName getNextAction() {
        final ActionName[] orderedActions = getOrderedActions();
        return IntStream.range(0, orderedActions.length)
                .filter(i -> orderedActions[i].executionSequence.equals(this.executionSequence))
                .filter(i -> i + 1 < orderedActions.length)
                .mapToObj(i -> orderedActions[i + 1])
                .findFirst()
                .orElse(null);

    }

    /**
     * Returns the first action name
     *
     * @return first action name
     */
    public static ActionName getFirstAction() {
        return getOrderedActions()[0];
    }

    private static ActionName[] getOrderedActions() {
        final ActionName[] orderedActions = values();
        Arrays.sort(orderedActions, Comparator.comparing(action -> action.executionSequence));
        return orderedActions;
    }
}
