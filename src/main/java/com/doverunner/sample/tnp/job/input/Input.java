package com.doverunner.sample.tnp.job.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Input {

    private static final int MAX_MULTI_OR_VIDEO_COUNT = 1;

    @JsonProperty("storage_id")
    private final String storageId;

    @JsonProperty("files")
    private final List<InputFile> files;

    @JsonProperty("subtitles")
    private final List<Subtitle> subtitles;

    private Input(String storageId, List<InputFile> files, List<Subtitle> subtitles) {
        this.storageId = storageId;
        this.files = files;
        this.subtitles = subtitles;
    }

    public List<InputFile> getFiles() {
        return files;
    }

    public static class Builder {

        private String storageId;
        private List<InputFile> files;
        private List<Subtitle> subtitles;

        public Builder storageId(String storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder files(List<InputFile> files) {
            this.files = files;
            return this;
        }

        public Builder subtitles(List<Subtitle> subtitles) {
            this.subtitles = subtitles;
            return this;
        }

        public Input build() {
            validateRequiredFields();
            validateFileTypeConstraints();
            return new Input(storageId, files, subtitles);
        }

        private void validateRequiredFields() {
            if (storageId == null || storageId.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'storageId' field is required when creating an Input.");
            }
            if (files == null || files.isEmpty()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'files' field is required when creating an Input.");
            }
        }

        private void validateFileTypeConstraints() {
            if (files.size() <= MAX_MULTI_OR_VIDEO_COUNT) {
                return;
            }

            long multiOrVideoCount = files.stream()
                    .filter(InputFile::isMultiOrVideoType)
                    .count();

            if (multiOrVideoCount > MAX_MULTI_OR_VIDEO_COUNT) {
                throw new TnpCustomException(ErrorCode.INVALID_VALUE,
                        "The number of files with file_type 'multi' or 'video' must not exceed "
                                + MAX_MULTI_OR_VIDEO_COUNT + " when creating an Input.");
            }
        }
    }
}