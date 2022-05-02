package com.api.actions;


import com.actions.model.ActionName;

/**
 * This enum defines the actions to be used in the action executor.
 */
public enum ActionType implements ActionName {

    ASSIGN_STORY(1),
    IMPLEMENT_REQUIREMENTS(2),
    SEND_PULL_REQUEST_EVENT(3),
    MERGE_CHANGES(4),
    SEND_STORY_COMPLETE_NOTIFICATION(5);

    private final Integer executionSequence;

    ActionType(final Integer executionSequence) {
        this.executionSequence = executionSequence;
    }

    @Override
    public Integer getExecutionSequence() {
        return executionSequence;
    }

    @Override
    public ActionName[] getActionNames() {
        return values();
    }

    @Override
    public String toString() {
        return this.name();
    }
}
