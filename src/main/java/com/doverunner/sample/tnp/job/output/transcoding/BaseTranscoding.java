package com.doverunner.sample.tnp.job.output.transcoding;

import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.FileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public abstract class BaseTranscoding implements Transcoding {

    @JsonProperty("track_id")
    private final String trackId;

    @JsonProperty("track_type")
    private final FileType trackType;

    @JsonProperty("track_name")
    private final String trackName;

    @JsonProperty("codec")
    private final String codec;

    protected BaseTranscoding(String trackId, FileType trackType, String trackName, String codec) {
        this.trackId = trackId;
        this.trackType = trackType;
        this.trackName = trackName;
        this.codec = codec;
        validateCommon();
    }

    private void validateCommon() {
        if (trackId == null || trackId.isBlank()) {
            throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                    "The 'trackId' field is required when creating Transcoding.");
        }
        if (trackType == null) {
            throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                    "The 'trackType' field is required when creating Transcoding.");
        }
        if (codec == null || codec.isBlank()) {
            throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                    "The 'codec' field is required when creating Transcoding.");
        }
    }
}
