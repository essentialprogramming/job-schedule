package com.actions.model;

/**
 * An action is a step of a workflow
 */
public interface Action<T> {

    /**
     * Returns the name of the action
     * @return ActionName enum
     */
    ActionName getName();

    /**
     * Executes the action for the passed target object.
     */
    ActionResult execute(final T target);

}
