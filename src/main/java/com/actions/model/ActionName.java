package com.actions.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * This interface must be implemented by an enum which defines
 * the actions to be used in the action executor.
 */
public interface ActionName {


    default boolean hasNextAction() {
        return getNextAction() != null;
    }

    /**
     * Gets next action.
     *
     * @return Next action or null if the current is already the last in the execution sequence.
     */
    default ActionName getNextAction() {
        final ActionName[] orderedActions = getOrderedActions();
        return IntStream.range(0, orderedActions.length)
                .filter(i -> orderedActions[i].getExecutionSequence().equals(this.getExecutionSequence()))
                .filter(i -> i + 1 < orderedActions.length)
                .mapToObj(i -> orderedActions[i + 1])
                .findFirst()
                .orElse(null);

    }

    default ActionName[] getOrderedActions() {
        final ActionName[] orderedActions = getActionNames();
        Arrays.sort(orderedActions, Comparator.comparing(ActionName::getExecutionSequence));
        return orderedActions;
    }

    Integer getExecutionSequence();
    ActionName[] getActionNames();

    @Override
    String toString();
}
