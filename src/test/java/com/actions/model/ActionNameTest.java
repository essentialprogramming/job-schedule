package com.actions.model;

import org.junit.jupiter.api.Test;

public class ActionNameTest {

    @Test
    void get_next_action(){
        ActionName actionName = ActionName.getFirstAction();
        System.out.println(actionName);

        actionName = actionName.getNextAction();
        System.out.println(actionName);

        actionName = actionName.getNextAction();
        System.out.println(actionName);

        actionName = actionName.getNextAction();
        System.out.println(actionName);

        actionName = actionName.getNextAction();
        System.out.println(actionName);


    }
}
