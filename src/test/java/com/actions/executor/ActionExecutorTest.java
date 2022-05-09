package com.actions.executor;

import com.actions.model.Action;
import com.actions.model.ActionName;
import com.actions.model.ActionResult;
import com.actions.utils.objects.Preconditions;

import java.util.ArrayList;
import java.util.Collections;

import com.api.actions.ActionType;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionExecutorTest<T> {

    private static MockedStatic<Preconditions> preconditionsMock;

    @Spy
    @InjectMocks
    private ActionExecutor<T> actionExecutor;

    @Mock
    private Action<T> testAction;

    @Mock
    private ActionName actionName;

    @Mock
    private T target;



    @BeforeAll
    static void init() {
        preconditionsMock = Mockito.mockStatic(Preconditions.class);
    }

    @Test
    void should_return_true_if_action_was_found_and_executed_successfully() {
        //given
        preconditionsMock.when(() -> Preconditions.assertNotNull(any(), any())).thenReturn(true);

        doReturn("test-action").when(actionName).toString();
        doReturn(actionName).when(testAction).getName();
        doReturn(ActionResult.<T>success()).when(testAction).execute(any());

        //when
        actionExecutor = new ActionExecutor<>(Collections.singletonList(testAction));
        val result = actionExecutor.executeAction(actionName, target);

        //then
        assertTrue(result.get("test-action").isSuccess());
    }

    @Test
    void should_return_action_result_error_if_action_was_not_found_and_execution_failed() {

        //given
        preconditionsMock.when(() -> Preconditions.assertNotNull(any(), any())).thenReturn(true);

        doReturn("test-action").when(actionName).toString();
        doReturn(actionName).when(testAction).getName();
        doReturn(ActionResult.<T>error("ACTION-001", "Couldn't find action test-action")).when(testAction).execute(any());

        //when
        actionExecutor = new ActionExecutor<>(Collections.singletonList(testAction));
        val result = actionExecutor.executeAction(actionName, target);

        //then
        assertTrue(result.get("test-action").isError());
    }

    @Test
    void should_return_action_result_error_if_runtime_exception_gets_thrown() {

        //given
        preconditionsMock.when(() -> Preconditions.assertNotNull(any(), any())).thenReturn(true);

        doReturn("test-action").when(actionName).toString();
        doReturn(actionName).when(testAction).getName();
        doThrow(new RuntimeException("Something went wrong while executing the action...")).when(testAction).execute(any());

        //when
        actionExecutor = new ActionExecutor<>(Collections.singletonList(testAction));
        val result = actionExecutor.executeAction(actionName, target);

        //then
        assertTrue(result.get("test-action").isError());
        assertEquals("RUNTIME-001", result.get("test-action").getFailure().getErrorCode());
        assertEquals("Something went wrong while executing the action...", result.get("test-action").getFailure().getDescription());
    }
}
