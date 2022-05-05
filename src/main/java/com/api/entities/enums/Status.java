package com.api.entities.enums;

public enum Status {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    IN_REVIEW("IN_REVIEW"),
    PR_REJECTED("PR_REJECTED"),
    NEEDS_IMPROVEMENT("NEEDS_IMPROVEMENT"),
    MERGED("MERGED"),
    DONE("DONE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
