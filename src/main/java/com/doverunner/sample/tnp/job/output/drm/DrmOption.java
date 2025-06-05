package com.doverunner.sample.tnp.job.output.drm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class DrmOption {

    @JsonProperty("multi_key")
    private final Boolean multiKey;

    @JsonProperty("max_sd_height")
    private final Integer maxSdHeight;

    @JsonProperty("max_hd_height")
    private final Integer maxHdHeight;

    @JsonProperty("max_uhd1_height")
    private final Integer maxUhd1Height;

    @JsonProperty("skip_audio_encryption")
    private final Boolean skipAudioEncryption;

    @JsonProperty("clear_lead")
    private final Integer clearLead;

    @JsonProperty("generate_tracktype_manifests")
    private final Boolean generateTrackTypeManifests;

    private DrmOption(Boolean multiKey, Integer maxSdHeight, Integer maxHdHeight, Integer maxUhd1Height,
                      Boolean skipAudioEncryption, Integer clearLead, Boolean generateTrackTypeManifests) {
        this.multiKey = multiKey;
        this.maxSdHeight = maxSdHeight;
        this.maxHdHeight = maxHdHeight;
        this.maxUhd1Height = maxUhd1Height;
        this.skipAudioEncryption = skipAudioEncryption;
        this.clearLead = clearLead;
        this.generateTrackTypeManifests = generateTrackTypeManifests;
    }

    public static class Builder {

        private Boolean multiKey = false;
        private Integer maxSdHeight = 480;
        private Integer maxHdHeight = 1080;
        private Integer maxUhd1Height = 2160;
        private Boolean skipAudioEncryption = false;
        private Integer clearLead = 0;
        private Boolean generateTrackTypeManifests = false;

        public Builder multiKey(Boolean multiKey) {
            this.multiKey = multiKey;
            return this;
        }

        public Builder maxSdHeight(Integer maxSdHeight) {
            this.maxSdHeight = maxSdHeight;
            return this;
        }

        public Builder maxHdHeight(Integer maxHdHeight) {
            this.maxHdHeight = maxHdHeight;
            return this;
        }

        public Builder maxUhd1Height(Integer maxUhd1Height) {
            this.maxUhd1Height = maxUhd1Height;
            return this;
        }

        public Builder skipAudioEncryption(Boolean skipAudioEncryption) {
            this.skipAudioEncryption = skipAudioEncryption;
            return this;
        }

        public Builder clearLead(Integer clearLead) {
            this.clearLead = clearLead;
            return this;
        }

        public Builder generateTrackTypeManifests(Boolean generateTrackTypeManifests) {
            this.generateTrackTypeManifests = generateTrackTypeManifests;
            return this;
        }

        public DrmOption build() {
            return new DrmOption(
                    multiKey,
                    maxSdHeight,
                    maxHdHeight,
                    maxUhd1Height,
                    skipAudioEncryption,
                    clearLead,
                    generateTrackTypeManifests
            );
        }
    }
}