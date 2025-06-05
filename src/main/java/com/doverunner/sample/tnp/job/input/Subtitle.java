package com.doverunner.sample.tnp.job.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subtitle {

    @JsonProperty("file_path")
    private final String filePath;

    @JsonProperty("language")
    private final String language;

    private Subtitle(String filePath, String language) {
        this.filePath = filePath;
        this.language = language;
    }

    public static class Builder {

        private String filePath;
        private String language;

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Subtitle build() {
            validate();
            return new Subtitle(filePath, language);
        }

        private void validate() {
            if (filePath == null || filePath.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'filePath' field is required when creating a Subtitle.");
            }
            if (language == null || language.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'language' field is required when creating a Subtitle.");
            }
        }
    }
}
