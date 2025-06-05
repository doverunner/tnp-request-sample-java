package com.doverunner.sample.tnp.job.output;

import com.doverunner.sample.tnp.job.output.drm.Drm;
import com.doverunner.sample.tnp.job.output.packaging.Packaging;
import com.doverunner.sample.tnp.job.output.transcoding.Transcoding;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.doverunner.sample.tnp.exception.ErrorCode;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Output {

    @JsonProperty("storage_id")
    private final String storageId;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("default_language")
    private final String defaultLanguage;

    @JsonProperty("transcodings")
    private final List<Transcoding> transcodings;

    @JsonProperty("packaging")
    private final Packaging packaging;

    @JsonProperty("drm")
    private final Drm drm;

    @JsonProperty("forensic_watermarking")
    private final ForensicWatermarking forensicWatermarking;

    @JsonProperty("job_noti_id")
    private final Integer jobNotiId;

    private Output(String storageId, String path, String defaultLanguage,
                   List<Transcoding> transcodings, Packaging packaging, Drm drm,
                   ForensicWatermarking forensicWatermarking, Integer jobNotiId) {
        this.storageId = storageId;
        this.path = path;
        this.defaultLanguage = defaultLanguage;
        this.transcodings = transcodings;
        this.packaging = packaging;
        this.drm = drm;
        this.forensicWatermarking = forensicWatermarking;
        this.jobNotiId = jobNotiId;
    }

    public List<Transcoding> getTranscodings() {
        return transcodings;
    }

    public static class Builder {

        private String storageId;
        private String path;
        private String defaultLanguage;
        private List<Transcoding> transcodings;
        private Packaging packaging;
        private Drm drm;
        private ForensicWatermarking forensicWatermarking;
        private Integer jobNotiId;

        public Builder storageId(String storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder defaultLanguage(String defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            return this;
        }

        public Builder transcodings(List<Transcoding> transcodings) {
            this.transcodings = transcodings;
            return this;
        }

        public Builder packaging(Packaging packaging) {
            this.packaging = packaging;
            return this;
        }

        public Builder drm(Drm drm) {
            this.drm = drm;
            return this;
        }

        public Builder forensicWatermarking(ForensicWatermarking forensicWatermarking) {
            this.forensicWatermarking = forensicWatermarking;
            return this;
        }

        public Builder jobNotiId(Integer jobNotiId) {
            this.jobNotiId = jobNotiId;
            return this;
        }

        public Output build() {
            validate();
            return new Output(storageId, path, defaultLanguage, transcodings, packaging, drm, forensicWatermarking, jobNotiId);
        }

        private void validate() {
            if (storageId == null || storageId.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'storageId' field is required when creating Output.");
            }

            if (path == null || path.isBlank()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'path' field is required when creating Output.");
            }

            if (transcodings == null || transcodings.isEmpty()) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'transcodings' field is required when creating Output.");
            }

            if (packaging == null) {
                throw new TnpCustomException(ErrorCode.MISSING_REQUIRED_VALUE,
                        "The 'packaging' field is required when creating Output.");
            }
        }
    }
}
