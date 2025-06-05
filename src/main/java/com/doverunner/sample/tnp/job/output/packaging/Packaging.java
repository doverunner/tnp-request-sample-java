package com.doverunner.sample.tnp.job.output.packaging;

import static java.lang.Boolean.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;

@JsonInclude(Include.NON_NULL)
public class Packaging {

    @JsonProperty("dash")
    private final Boolean dash;

    @JsonProperty("hls")
    private final Boolean hls;

    @JsonProperty("cmaf")
    private final Boolean cmaf;

    @JsonProperty("option")
    private final PackagingOption option;

    private Packaging(Boolean dash, Boolean hls, Boolean cmaf, PackagingOption option) {
        this.dash = dash;
        this.hls = hls;
        this.cmaf = cmaf;
        this.option = option;
    }

    public static class Builder {

        private Boolean dash;
        private Boolean hls;
        private Boolean cmaf;
        private PackagingOption option;

        public Builder dash(Boolean dash) {
            this.dash = dash;
            return this;
        }

        public Builder hls(Boolean hls) {
            this.hls = hls;
            return this;
        }

        public Builder cmaf(Boolean cmaf) {
            this.cmaf = cmaf;
            return this;
        }

        public Builder option(PackagingOption option) {
            this.option = option;
            return this;
        }

        public Packaging build() {
            validate();
            return new Packaging(dash, hls, cmaf, option);
        }

        private void validate() {
            if ((dash == null || !dash) && (hls == null || !hls) && (cmaf == null || !cmaf)) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "At least one of 'dash', 'hls', or 'cmaf' must be true when creating Packaging.");
            }

            if (Boolean.TRUE.equals(cmaf) && (Boolean.TRUE.equals(dash) || Boolean.TRUE.equals(hls))) {
                throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                        "If 'cmaf' is true, both 'dash' and 'hls' must be false or null when creating Packaging.");
            }
        }
    }
}
