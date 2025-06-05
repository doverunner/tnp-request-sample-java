package com.doverunner.sample.tnp.job;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileType {

    AUDIO("audio"), VIDEO("video"), MULTI("multi");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
