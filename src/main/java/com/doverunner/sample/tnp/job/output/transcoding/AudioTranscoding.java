package com.doverunner.sample.tnp.job.output.transcoding;

import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.FileType;
import com.doverunner.sample.tnp.job.Track;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class AudioTranscoding extends BaseTranscoding {

    @JsonProperty("language")
    private final String language;

    @JsonProperty("sources")
    private final List<Track> sources;

    public AudioTranscoding(String trackId, FileType trackType, String trackName, String codec, String language,
                            List<Track> sources) {
        super(trackId, trackType, trackName, codec);
        this.language = language;
        this.sources = sources;
    }

    @Override
    public boolean hasAudioInfo() {
        return true;
    }

    public static class Builder {

        private String trackId;
        private FileType trackType;
        private String trackName;
        private String codec;
        private String language;
        private List<Track> sources;

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

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder sources(List<Track> sources) {
            this.sources = sources;
            return this;
        }

        public AudioTranscoding build() {
            validate();
            return new AudioTranscoding(trackId, trackType, trackName, codec, language, sources);
        }

        private void validate() {
            if (sources == null || sources.isEmpty()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'sources' field is required when the track_type is 'audio' during Transcoding creation.");
            }
        }
    }
}
