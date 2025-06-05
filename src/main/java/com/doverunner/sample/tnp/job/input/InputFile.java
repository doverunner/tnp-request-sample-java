package com.doverunner.sample.tnp.job.input;

import com.doverunner.sample.tnp.job.FileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InputFile {

    @JsonProperty("file_type")
    private final FileType fileType;

    @JsonProperty("file_path")
    private final String filePath;

    @JsonProperty("audios")
    private final List<Audio> audios;

    private InputFile(FileType fileType, String filePath, List<Audio> audios) {
        this.fileType = fileType;
        this.filePath = filePath;
        this.audios = audios;
    }

    @JsonIgnore
    public boolean isMultiOrVideoType() {
        return fileType == FileType.VIDEO || fileType == FileType.MULTI;
    }

    public boolean hasAudioInfo() {
        return fileType == FileType.AUDIO || fileType == FileType.MULTI;
    }

    public static class Builder {

        private FileType fileType;
        private String filePath;
        private List<Audio> audios;

        public Builder fileType(FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder audios(List<Audio> audios) {
            this.audios = audios;
            return this;
        }

        public InputFile build() {
            validate();
            return new InputFile(fileType, filePath, audios);
        }

        private void validate() {
            if (fileType == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'fileType' field is required when creating an InputFile.");
            }

            if (filePath == null || filePath.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'filePath' field is required when creating an InputFile.");
            }

            if ((fileType == FileType.AUDIO || fileType == FileType.MULTI) &&
                    (audios == null || audios.isEmpty())) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'audios' field is required when the fileType is 'audio' or 'multi'.");
            }
        }
    }
}
