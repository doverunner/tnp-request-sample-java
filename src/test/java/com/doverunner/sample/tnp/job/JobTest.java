package com.doverunner.sample.tnp.job;

import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static com.doverunner.sample.tnp.job.Job.Builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.doverunner.sample.tnp.job.output.transcoding.VideoTranscoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.input.Audio;
import com.doverunner.sample.tnp.job.input.Input;
import com.doverunner.sample.tnp.job.input.InputFile;
import com.doverunner.sample.tnp.job.input.Subtitle;
import com.doverunner.sample.tnp.job.output.drm.Drm;
import com.doverunner.sample.tnp.job.output.drm.DrmOption;
import com.doverunner.sample.tnp.job.output.Output;
import com.doverunner.sample.tnp.job.output.packaging.Packaging;
import com.doverunner.sample.tnp.job.output.packaging.PackagingOption;
import com.doverunner.sample.tnp.job.output.packaging.SubtitleFormat;
import com.doverunner.sample.tnp.job.output.transcoding.Transcoding;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JobTest {

    private static final Logger logger = LoggerFactory.getLogger(JobTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private Input input;
    private Output output;

    @BeforeEach
    void setup() {
        input = buildInput();
        output = buildOutput();
    }

    @Nested
    @DisplayName("정상 생성 케이스")
    class ValidJobBuilderTest {

        @Test
        @DisplayName("모든 필수 필드가 존재하면 Job 객체 생성에 성공해야 한다.")
        void build_should_return_job() {
            // given
            String jobName = "job_name";
            String contentId = "content_001";

            // when
            Job job = new Builder()
                    .jobName(jobName)
                    .contentId(contentId)
                    .input(input)
                    .output(output)
                    .build();

            // then
            assertThat(job).isNotNull();
        }

        @Test
        @DisplayName("Job 객체를 JSON으로 직렬화했을 때 필드가 정확히 포함되어야 한다.")
        void build_should_serialize_job_to_valid_json() throws Exception {
            // given
            String jobName = "job_name";
            String contentId = "content_001";
            Job job = new Builder()
                    .jobName(jobName)
                    .contentId(contentId)
                    .input(input)
                    .output(output)
                    .build();

            String expected = """
                    {
                      "job_name" : "job_name",
                      "content_id" : "content_001",
                      "input" : {
                        "storage_id" : "storage-002",
                        "files" : [ {
                          "file_type" : "video",
                          "file_path" : "input.mp4",
                          "audios" : [ {
                            "in" : {
                              "track" : 0
                            },
                            "remap" : {
                              "track" : 1
                            }
                          } ]
                        } ],
                        "subtitles" : [ {
                          "file_path" : "subtitle.vtt",
                          "language" : "eng"
                        } ]
                      },
                      "output" : {
                        "storage_id" : "storage-001",
                        "path" : "/output/path",
                        "transcodings" : [ {
                          "track_id" : "video_1",
                          "track_type" : "video",
                          "track_name" : "myTrack",
                          "codec" : "h264",
                          "height" : 1080,
                          "width" : 1920,
                          "bitrate" : 3000
                        } ],
                        "packaging" : {
                          "dash" : true,
                          "option" : {
                            "min_buffer_time" : 3,
                            "enable_average_bandwidth_mpd" : true,
                            "subtitle_format" : {
                              "dash" : "text.vtt"
                            }
                          }
                        },
                        "drm" : {
                          "enabled" : true,
                          "option" : {
                            "multi_key" : true,
                            "max_sd_height" : 360,
                            "max_hd_height" : 720,
                            "max_uhd1_height" : 2160,
                            "skip_audio_encryption" : true,
                            "clear_lead" : 5,
                            "generate_tracktype_manifests" : true
                          }
                        }
                      }
                    }""";
                    
            // when
            String actual = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(job);

            // then
            logger.debug("Serialized JSON: {}", actual);
            assertThat(mapper.readTree(actual)).isEqualTo(mapper.readTree(expected));
        }
    }

    @Nested
    @DisplayName("필수 필드 누락 케이스")
    class RequiredFieldValidationTest {

        @Test
        @DisplayName("jobName이 누락되면 예외가 발생해야 한다.")
        void build_should_throw_when_jobName_is_missing() throws Exception {
            // given
            String contentId = "content_001";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .contentId(contentId)
                        .input(input)
                        .output(output)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'jobName' field is required when creating a Job.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("contentId가 누락되면 예외가 발생해야 한다.")
        void build_should_throw_when_contentId_is_missing() throws Exception {
            // given
            String jobName = "job_name";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .jobName(jobName)
                        .input(input)
                        .output(output)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'contentId' field is required when creating a Job.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("input이 누락되면 예외가 발생해야 한다.")
        void build_should_throw_when_input_is_missing() throws Exception {
            // given
            String jobName = "job_name";
            String contentId = "content_001";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .jobName(jobName)
                        .contentId(contentId)
                        .output(output)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'input' field is required when creating a Job.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("output이 누락되면 예외가 발생해야 한다.")
        void build_should_throw_when_output_is_missing() throws Exception {
            // given
            String jobName = "job_name";
            String contentId = "content_001";

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .jobName(jobName)
                        .contentId(contentId)
                        .input(input)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'output' field is required when creating a Job.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("input에 입력한 오디오 정보와 output에 입력한 오디오 정보가 다르다면 지정한 예외가 발생해야 한다.")
        void build_should_throw_when_input_audio_info_And_output_audio_info_different() throws Exception {
            // given
            Input testInput = new Input.Builder()
                    .storageId("storage-002")
                    .files(List.of(
                            new InputFile.Builder()
                                    .fileType(FileType.AUDIO)   // file_type : audio
                                    .filePath("input.mp4")
                                    .audios(buildAudios())      // 오디오 정보 포함
                                    .build()
                    ))
                    .subtitles(buildSubtitles())
                    .build();

            Output testOutput = new Output.Builder()
                    .storageId("storage-001")
                    .path("/output/path")
                    .transcodings(List.of(
                            new VideoTranscoding.Builder()
                                    .trackId("video_1")
                                    .trackType(FileType.VIDEO)  // file_type : video
                                    .trackName("myTrack")
                                    .codec("h264")
                                    .height(1080)
                                    .width(1920)
                                    .bitrate(3000)
                                    .build()))
                    .packaging(buildPackaging())
                    .drm(buildDrm())
                    .build();

            // when
            Throwable throwable = catchThrowable(() -> {
                new Builder()
                        .jobName("jobName")
                        .contentId("contentId")
                        .input(testInput)
                        .output(testOutput)
                        .build();
            });

            // then
            assertThat(throwable).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) throwable;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The audio information in 'input' and 'output' must be consistent when creating a Job.");

            logger.debug(mapper.writeValueAsString(ex));
        }
    }

    private Input buildInput() {
        return new Input.Builder()
                .storageId("storage-002")
                .files(buildFiles())
                .subtitles(buildSubtitles())
                .build();
    }

    private List<InputFile> buildFiles() {
        return List.of(
            new InputFile.Builder()
                    .fileType(FileType.VIDEO)
                    .filePath("input.mp4")
                    .audios(buildAudios())
                    .build()
        );
    }

    private List<Audio> buildAudios() {
        return List.of(
                new Audio.Builder()
                        .in(new Track(0))
                        .remap(new Track(1))
                        .build()
        );
    }

    private List<Subtitle> buildSubtitles() {
        return List.of(
                new Subtitle.Builder()
                        .filePath("subtitle.vtt")
                        .language("eng")
                        .build()
        );
    }

    private Output buildOutput() {
        return new Output.Builder()
                .storageId("storage-001")
                .path("/output/path")
                .transcodings(buildTranscodings())
                .packaging(buildPackaging())
                .drm(buildDrm())
                .build();
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