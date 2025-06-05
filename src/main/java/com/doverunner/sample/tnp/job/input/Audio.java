package com.doverunner.sample.tnp.job.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.Track;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audio {

    @JsonProperty("in")
    private final Track in;

    @JsonProperty("remap")
    private final Track remap;

    private Audio(Track in, Track remap) {
        this.in = in;
        this.remap = remap;
    }

    public static class Builder {

        private Track in;
        private Track remap;

        public Builder in(Track in) {
            this.in = in;
            return this;
        }

        public Builder remap(Track remap) {
            this.remap = remap;
            return this;
        }

        public Audio build() {
            validate();
            return new Audio(in, remap);
        }

        private void validate() {
            if (in == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'in' field is required when creating an Audio.");
            }
            if (remap == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'remap' field is required when creating an Audio.");
            }
        }
    }
}
