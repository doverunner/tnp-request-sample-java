package com.doverunner.sample.tnp.job.output.packaging;

import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class SubtitleFormat {

    @JsonProperty("dash")
    private final String dash;

    @JsonProperty("hls")
    private final String hls;

    @JsonProperty("cmaf")
    private final String cmaf;

    private SubtitleFormat(String dash, String hls, String cmaf) {
        this.dash = dash;
        this.hls = hls;
        this.cmaf = cmaf;
    }

    public static class Builder {

        private String dash;
        private String hls;
        private String cmaf;

        public Builder dash(String dash) {
            this.dash = dash;
            return this;
        }

        public Builder hls(String hls) {
            this.hls = hls;
            return this;
        }

        public Builder cmaf(String cmaf) {
            this.cmaf = cmaf;
            return this;
        }

        public SubtitleFormat build() {
            validate();
            return new SubtitleFormat(dash, hls, cmaf);
        }

        private void validate() {
            if ((dash == null || dash.isBlank()) && (hls == null || hls.isBlank()) && (cmaf == null || cmaf.isBlank())) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "At least one of 'dash', 'hls', or 'cmaf' must be provided when creating a SubtitleFormat.");
            }
        }
    }
}