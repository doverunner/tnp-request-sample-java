package com.doverunner.sample.tnp.job;

import com.doverunner.sample.tnp.job.input.InputFile;
import com.doverunner.sample.tnp.job.output.transcoding.Transcoding;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.input.Input;
import com.doverunner.sample.tnp.job.output.Output;

@JsonInclude(Include.NON_NULL)
public class Job {

    @JsonProperty("job_name")
    private final String jobName;

    @JsonProperty("content_id")
    private final String contentId;

    @JsonProperty("input")
    private final Input input;

    @JsonProperty("output")
    private final Output output;

    private Job(String jobName, String contentId, Input input, Output output) {
        this.jobName = jobName;
        this.contentId = contentId;
        this.input = input;
        this.output = output;
    }

    public static class Builder {

        private String jobName;
        private String contentId;
        private Input input;
        private Output output;

        public Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder input(Input input) {
            this.input = input;
            return this;
        }

        public Builder output(Output output) {
            this.output = output;
            return this;
        }

        public Job build() {
            validateRequiredFields();
            validateAudioInfo();
            return new Job(jobName, contentId, input, output);
        }

        private void validateRequiredFields() {
            if (jobName == null || jobName.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'jobName' field is required when creating a Job.");
            }
            if (contentId == null || contentId.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'contentId' field is required when creating a Job.");
            }
            if (input == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'input' field is required when creating a Job.");
            }
            if (output == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'output' field is required when creating a Job.");
            }
        }

        private void validateAudioInfo() {
            boolean hasAudioInfoInInput = input.getFiles().stream().anyMatch(InputFile::hasAudioInfo);
            boolean hasAudioInfoInOutput = output.getTranscodings().stream().anyMatch(Transcoding::hasAudioInfo);

            if (hasAudioInfoInInput != hasAudioInfoInOutput) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The audio information in 'input' and 'output' must be consistent when creating a Job.");
            }
        }
    }
}