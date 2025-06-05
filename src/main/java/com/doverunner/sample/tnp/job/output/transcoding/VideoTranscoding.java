package com.doverunner.sample.tnp.job.output.transcoding;

import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.FileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class VideoTranscoding extends BaseTranscoding {

    @JsonProperty("height")
    private final Integer height;

    @JsonProperty("width")
    private final Integer width;

    @JsonProperty("bitrate_mode")
    private final BitrateMode bitrateMode;

    @JsonProperty("bitrate")
    private final Integer bitrate;

    @JsonProperty("min_bitrate")
    private final Integer minBitrate;

    @JsonProperty("max_bitrate")
    private final Integer maxBitrate;

    @JsonProperty("crf")
    private final Integer crf;

    @JsonProperty("bandwidth")
    private final Integer bandwidth;

    public VideoTranscoding(String trackId, FileType trackType, String trackName, String codec, Integer height,
                            Integer width, BitrateMode bitrateMode, Integer bitrate, Integer minBitrate,
                            Integer maxBitrate, Integer crf, Integer bandwidth) {
        super(trackId, trackType, trackName, codec);
        this.height = height;
        this.width = width;
        this.bitrateMode = bitrateMode;
        this.bitrate = bitrate;
        this.minBitrate = minBitrate;
        this.maxBitrate = maxBitrate;
        this.crf = crf;
        this.bandwidth = bandwidth;
    }

    @Override
    public boolean hasAudioInfo() {
        return false;
    }

    public static class Builder {

        private String trackId;
        private FileType trackType;
        private String trackName;
        private String codec;
        private Integer height;
        private Integer width;
        private BitrateMode bitrateMode;
        private Integer bitrate;
        private Integer minBitrate;
        private Integer maxBitrate;
        private Integer crf;
        private Integer bandwidth;

        public Builder trackId(String trackId) {
            this.trackId = trackId;
            return this;
        }

        public Builder trackType(FileType trackType) {
            this.trackType = trackType;
            return this;
        }

        public Builder trackName(String trackName) {
            this.trackName = trackName;
            return this;
        }

        public Builder codec(String codec) {
            this.codec = codec;
            return this;
        }

        public Builder height(Integer height) {
            this.height = height;
            return this;
        }

        public Builder width(Integer width) {
            this.width = width;
            return this;
        }

        public Builder bitrateMode(BitrateMode bitrateMode) {
            this.bitrateMode = bitrateMode;
            return this;
        }

        public Builder bitrate(Integer bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        public Builder minBitrate(Integer minBitrate) {
            this.minBitrate = minBitrate;
            return this;
        }

        public Builder maxBitrate(Integer maxBitrate) {
            this.maxBitrate = maxBitrate;
            return this;
        }

        public Builder crf(Integer crf) {
            this.crf = crf;
            return this;
        }

        public Builder bandwidth(Integer bandwidth) {
            this.bandwidth = bandwidth;
            return this;
        }

        public VideoTranscoding build() {
            validate();
            return new VideoTranscoding(
                    trackId, trackType, trackName, codec, height, width, bitrateMode,
                    bitrate, minBitrate, maxBitrate, crf, bandwidth);
        }

        private void validate() {
            validateVideoDimensions();
            validateBitrateConditions();
        }

        private void validateVideoDimensions() {
            if (height == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'height' field is required when the trackType is 'video' during Transcoding creation.");
            }
            if (width == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'width' field is required when the trackType is 'video' during Transcoding creation.");
            }
            if (height % 2 != 0) {
                throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                        "The 'height' value must be an even number when creating Transcoding.");
            }
            if (width % 2 != 0) {
                throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                        "The 'width' value must be an even number when creating Transcoding.");
            }
        }

        private void validateBitrateConditions() {
            if ((bitrateMode == null || bitrateMode == BitrateMode.CBR) && bitrate == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'bitrate' field is required when 'bitrate_mode' is CBR during Transcoding creation.");
            }

            else if (bitrateMode == BitrateMode.VBR) {
                boolean isOnlyBitrate = bitrate != null && minBitrate == null && maxBitrate == null && crf == null;
                boolean isOnlyMinMax = bitrate == null && minBitrate != null && maxBitrate != null && crf == null;
                boolean isOnlyCrf = bitrate == null && minBitrate == null && maxBitrate == null && crf != null;

                if (isOnlyBitrate || isOnlyMinMax || isOnlyCrf) {
                    return;
                }

                if ((minBitrate == null) != (maxBitrate == null)) {
                    throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                            "When 'bitrate_mode' is VBR, both 'min_bitrate' and 'max_bitrate' must be set together.");
                }

                if (bitrate == null && minBitrate == null) {
                    throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                            "When 'bitrate_mode' is VBR, one of the following must be provided: 'bitrate', 'min_bitrate and max_bitrate', or 'crf'.");
                }

                throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                        "When 'bitrate_mode' is VBR, only one of the following should be provided: 'bitrate', 'min_bitrate and max_bitrate', or 'crf'.");
            }
        }
    }
}
