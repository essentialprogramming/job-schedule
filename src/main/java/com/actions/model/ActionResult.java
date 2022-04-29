package com.actions.model;

import com.actions.utils.model.Failure;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ActionResult {

    private final ActionStatus status;
    private final String errorCode;
    private final String errorMessage;

    public boolean isSuccess() {
        return ActionStatus.SUCCESS.equals(status);
    }

    public boolean isError() {
        return ActionStatus.FAILED.equals(status);
    }

    public static ActionResult success() {
        return ActionResult.builder().status(ActionStatus.SUCCESS).build();
    }

    public static ActionResult error(final String errorCode, final String errorMessage) {
        return ActionResult.builder()
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .status(ActionStatus.FAILED).build();
    }

    public static ActionResult error(final Failure failure) {
        return ActionResult.builder()
                .errorMessage(failure.getErrorCode())
                .errorCode(failure.getDescription())
                .status(ActionStatus.FAILED).build();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
