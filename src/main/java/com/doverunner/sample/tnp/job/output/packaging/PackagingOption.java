package com.doverunner.sample.tnp.job.output.packaging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;

@JsonInclude(Include.NON_NULL)
public class PackagingOption {

    @JsonProperty("min_buffer_time")
    private final Integer minBufferTime;

    @JsonProperty("enable_average_bandwidth_mpd")
    private final Boolean enableAverageBandwidthMpd;

    @JsonProperty("subtitle_format")
    private final SubtitleFormat subtitleFormat;

    private PackagingOption(Integer minBufferTime, Boolean enableAverageBandwidthMpd, SubtitleFormat subtitleFormat) {
        this.minBufferTime = minBufferTime;
        this.enableAverageBandwidthMpd = enableAverageBandwidthMpd;
        this.subtitleFormat = subtitleFormat;
    }

    public static class Builder {

        private static final int DEFAULT_MIN_BUFFER_TIME = 2;

        private Integer minBufferTime;
        private Boolean enableAverageBandwidthMpd = false;
        private SubtitleFormat subtitleFormat;

        public Builder minBufferTime(Integer minBufferTime) {
            this.minBufferTime = minBufferTime;
            return this;
        }

        public Builder enableAverageBandwidthMpd(Boolean enableAverageBandwidthMpd) {
            this.enableAverageBandwidthMpd = enableAverageBandwidthMpd;
            return this;
        }

        public Builder subtitleFormat(SubtitleFormat subtitleFormat) {
            this.subtitleFormat = subtitleFormat;
            return this;
        }

        public PackagingOption build() {
            validate();
            return new PackagingOption(minBufferTime, enableAverageBandwidthMpd, subtitleFormat);
        }

        private void validate() {
            if (minBufferTime != null && minBufferTime < DEFAULT_MIN_BUFFER_TIME) {
                throw new TnpCustomException(ErrorCode.OUT_OF_RANGE_VALUE,
                        "The 'minBufferTime' must be at least " + DEFAULT_MIN_BUFFER_TIME + " when creating a PackagingOption.");
            }
        }
    }
}