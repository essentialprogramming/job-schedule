package com.api.entities.enums;

public enum Status {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    PULL_REQUEST("PULL_REQUEST"),
    PR_REJECTED("PR_REJECTED"),
    CHANGES_REQUIRED("CHANGES_REQUIRED"),
    MERGED("MERGED"),
    DONE("DONE");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
