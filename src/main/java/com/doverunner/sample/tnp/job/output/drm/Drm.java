package com.doverunner.sample.tnp.job.output.drm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Drm {

    @JsonProperty("enabled")
    private final Boolean enabled;

    @JsonProperty("option")
    private final DrmOption option;

    private Drm(Boolean enabled, DrmOption option) {
        this.enabled = enabled;
        this.option = option;
    }

    public static class Builder {

        private Boolean enabled;
        private DrmOption option;

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder option(DrmOption option) {
            this.option = option;
            return this;
        }

        public Drm build() {
            return new Drm(enabled, option);
        }
    }
}