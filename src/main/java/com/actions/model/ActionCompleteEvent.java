package com.actions.model;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class ActionCompleteEvent<T> extends ApplicationEvent {

    private ActionName actionName;
    private T target;

    public ActionCompleteEvent(Object source, ActionName actionName, T target) {
        super(source);
        this.actionName = actionName;
        this.target = target;
    }
}
