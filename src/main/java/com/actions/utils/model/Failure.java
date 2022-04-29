package com.actions.utils.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class Failure {

    private final String code;
    private final String description;

    public String getErrorCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

}
