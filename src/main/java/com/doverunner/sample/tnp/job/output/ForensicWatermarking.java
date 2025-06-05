package com.doverunner.sample.tnp.job.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ForensicWatermarking {

    @JsonProperty("enabled")
    private Boolean enabled;

    public ForensicWatermarking() {
        this(false);
    }

    public ForensicWatermarking(Boolean enabled) {
        this.enabled = enabled;
    }
}
