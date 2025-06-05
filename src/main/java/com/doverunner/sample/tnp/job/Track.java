package com.doverunner.sample.tnp.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Track {

    private static final int DEFAULT_TRACK_INDEX = 0;

    @JsonProperty("track")
    private final int index;

    public Track() {
        this(DEFAULT_TRACK_INDEX);
    }

    public Track(int index) {
        validate(index);
        this.index = index;
    }

    private void validate(int index) {
        if (index < DEFAULT_TRACK_INDEX) {
            throw new TnpCustomException(ErrorCode.OUT_OF_RANGE_VALUE,
                    "The track index must be greater than or equal to 0.");
        }
    }
}