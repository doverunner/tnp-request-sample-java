package com.doverunner.sample.tnp.job.output.transcoding;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BitrateMode {
    CBR("cbr"), VBR("vbr");

    private final String value;

    BitrateMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
