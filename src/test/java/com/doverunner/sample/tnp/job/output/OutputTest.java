package com.doverunner.sample.tnp.job.output;

import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.doverunner.sample.tnp.job.FileType;
import com.doverunner.sample.tnp.job.output.drm.Drm;
import com.doverunner.sample.tnp.job.output.drm.DrmOption;
import com.doverunner.sample.tnp.job.output.packaging.Packaging;
import com.doverunner.sample.tnp.job.output.packaging.PackagingOption;
import com.doverunner.sample.tnp.job.output.packaging.SubtitleFormat;
import com.doverunner.sample.tnp.job.output.transcoding.Transcoding;
import com.doverunner.sample.tnp.job.output.transcoding.VideoTranscoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.output.Output.Builder;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OutputTest {

    private static final Logger logger = LoggerFactory.getLogger(OutputTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private List<Transcoding> transcodings;
    private Packaging packaging;
    private Drm drm;

    @BeforeEach
    void setup() {
        transcodings = buildTranscodings();
        packaging = buildPackaging();
        drm = buildDrm();
    }

    @Nested
    @DisplayName("정상 생성 케이스")
    class ValidOutputCreationTest {

        @Test
        @DisplayName("필수값이 모두 존재하면 Output 객체 생성에 성공해야 한다.")
        void build_should_return_output() throws Exception {
            // given
            String storageId = "storage-001";
            String path = "/output/path";

            // when
            Output output = new Output.Builder()
                    .storageId(storageId)
                    .path(path)
                    .transcodings(transcodings)
                    .packaging(packaging)
                    .drm(drm)
                    .build();

            // then
            assertThat(output).isNotNull();
            logger.debug(mapper.writeValueAsString(output));
        }

        @Test
        @DisplayName("Output 객체를 JSON으로 직렬화했을 때 필드가 정확히 포함되어야 한다.")
        void build_should_serialize_output_to_valid_json() throws Exception {
            // given
            String storageId = "storage-001";
            String path = "/output/path";
            Output output = new Output.Builder()
                    .storageId(storageId)
                    .path(path)
                    .transcodings(transcodings)
                    .packaging(packaging)
                    .drm(drm)
                    .build();

            String expected = """
                    {
                      "storage_id": "storage-001",
                      "path": "/output/path",
                      "transcodings": [
                        {
                          "track_id": "video_1",
                          "track_type": "video",
                          "track_name": "myTrack",
                          "codec": "h264",
                          "height": 1080,
                          "width": 1920,
                          "bitrate": 3000
                        }
                      ],
                      "packaging": {
                        "dash": true,
                        "option": {
                          "min_buffer_time": 3,
                          "enable_average_bandwidth_mpd": true,
                          "subtitle_format": { "dash": "text.vtt" }
                        }
                      },
                      "drm": {
                        "enabled": true,
                        "option": {
                          "multi_key": true,
                          "max_sd_height": 360,
                          "max_hd_height": 720,
                          "max_uhd1_height": 2160,
                          "skip_audio_encryption": true,
                          "clear_lead": 5,
                          "generate_tracktype_manifests": true
                        }
                      }
                    }""";

            // when
            String actual = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);

            // then
            logger.debug("Serialized JSON: {}", actual);
            assertThat(mapper.readTree(actual)).isEqualTo(mapper.readTree(expected));
        }
    }

    @Nested
    @DisplayName("필수 필드 누락 시 예외 발생 테스트")
    class RequiredFieldValidationTest {

        @Test
        @DisplayName("storageId가 없으면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_storageId_is_missing() throws Exception {
            // given
            String path = "/output/path";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .path(path)
                        .transcodings(transcodings)
                        .packaging(packaging)
                        .drm(drm)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'storageId' field is required when creating Output.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("path가 없으면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_path_is_missing() throws Exception {
            // given
            String storageId = "storage-001";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .storageId(storageId)
                        .transcodings(transcodings)
                        .packaging(packaging)
                        .drm(drm)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'path' field is required when creating Output.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("transcodings가 없으면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_transcodings_is_missing() throws Exception {
            // given
            String storageId = "storage-001";
            String path = "/output/path";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .storageId(storageId)
                        .path(path)
                        .packaging(packaging)
                        .drm(drm)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'transcodings' field is required when creating Output.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("packaging이 없으면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_packaging_is_missing() throws Exception {
            // given
            String storageId = "storage-001";
            String path = "/output/path";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .storageId(storageId)
                        .path(path)
                        .transcodings(transcodings)
                        .drm(drm)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'packaging' field is required when creating Output.");

            logger.debug(mapper.writeValueAsString(ex));
        }
    }

    private List<Transcoding> buildTranscodings() {
        return List.of(
                new VideoTranscoding.Builder()
                        .trackId("video_1")
                        .trackType(FileType.VIDEO)
                        .trackName("myTrack")
                        .codec("h264")
                        .height(1080)
                        .width(1920)
                        .bitrate(3000)
                        .build()
        );
    }

    private Packaging buildPackaging() {
        return new Packaging.Builder()
                .dash(true)
                .option(new PackagingOption.Builder()
                        .minBufferTime(3)
                        .enableAverageBandwidthMpd(true)
                        .subtitleFormat(new SubtitleFormat.Builder()
                                .dash("text.vtt")
                                .build())
                        .build())
                .build();
    }

    private Drm buildDrm() {
        return new Drm.Builder()
                .enabled(true)
                .option(new DrmOption.Builder()
                        .multiKey(true)
                        .maxSdHeight(360)
                        .maxHdHeight(720)
                        .maxUhd1Height(2160)
                        .skipAudioEncryption(true)
                        .clearLead(5)
                        .generateTrackTypeManifests(true)
                        .build())
                .build();
    }
}