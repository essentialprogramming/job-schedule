package com.example.api.entities.enums;

public enum Status {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    PULL_REQUEST("PULL_REQUEST"),
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
