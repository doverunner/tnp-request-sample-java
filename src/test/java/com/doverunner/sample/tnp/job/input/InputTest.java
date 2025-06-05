package com.doverunner.sample.tnp.job.input;

import static com.doverunner.sample.tnp.exception.ErrorCode.INVALID_VALUE;
import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.doverunner.sample.tnp.job.FileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.Track;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InputTest {

    private static final Logger logger = LoggerFactory.getLogger(InputTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private List<InputFile> validFiles;
    private List<Subtitle> validSubtitles;

    @BeforeEach
    void setUp() {
        List<Audio> audios = List.of(
                new Audio.Builder()
                        .in(new Track(0))
                        .remap(new Track(1))
                        .build()
        );

        validFiles = List.of(
                new InputFile.Builder()
                        .fileType(FileType.VIDEO)
                        .filePath("input.mp4")
                        .audios(audios)
                        .build()
        );
        validSubtitles = List.of(
                new Subtitle.Builder()
                        .filePath("subtitle.vtt")
                        .language("eng")
                        .build()
        );
    }

    @Nested
    @DisplayName("정상 생성 케이스")
    class ValidInputCreationTest {

        @Test
        @DisplayName("필수값이 모두 주어지면 Input 객체 생성에 성공해야 한다.")
        void build_should_return_input() throws Exception {
            // given
            String storageId = "storageId";
            List<InputFile> files = validFiles;
            List<Subtitle> subtitles = validSubtitles;

            // when
            Input input = new Input.Builder()
                    .storageId(storageId)
                    .files(files)
                    .subtitles(subtitles)
                    .build();

            // then
            assertThat(input).isNotNull();
            logger.debug(mapper.writeValueAsString(input));
        }

        @Test
        @DisplayName("Input 객체를 JSON으로 직렬화하면 필수 필드가 포함되어야 한다.")
        void serialize_input_to_json_should_contain_required_fields() throws Exception {
            // given
            String storageId = "storageId";
            List<InputFile> files = validFiles;
            List<Subtitle> subtitles = validSubtitles;

            // when
            Input input = new Input.Builder()
                    .storageId(storageId)
                    .files(files)
                    .subtitles(subtitles)
                    .build();

            // then
            String json = mapper.writeValueAsString(input);
            logger.debug(json);
        }
    }

    @Nested
    @DisplayName("예외 발생 테스트")
    class InputValidationExceptionTest {

        @Test
        @DisplayName("Input 생성 시 storageId가 null이라면 지정한 TnPCustomException이 발생해야 한다.")
        void build_should_throw_exception_when_input_has_no_storageId() throws Exception {
            // given
            List<InputFile> files = validFiles;

            // when
            Throwable thrown = catchThrowable(() ->
                    new Input.Builder()
                            .files(files)
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getDetailMessage()).isEqualTo("The 'storageId' field is required when creating an Input.");
            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("Input 생성 시 files가 없다면 지정한 TnPCustomException이 발생해야 한다.")
        void build_should_throw_exception_when_input_has_no_files() throws Exception {
            // given
            String storageId = "storageId";

            // when
            Throwable thrown = catchThrowable(() ->
                    new Input.Builder()
                            .storageId(storageId)
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getDetailMessage()).isEqualTo("The 'files' field is required when creating an Input.");
            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("Input 생성 시 files의 file_type이 multi 혹은 video의 개수가 2 이상이라면 지정한 TnPCustomException이 발생해야 한다.")
        void build_should_throw_exception_when_multiOrVideoCount_is_over_than_two() throws Exception {
            // given
            InputFile videoFile = new InputFile.Builder()
                    .fileType(FileType.VIDEO)
                    .filePath("input/demo")
                    .build();

            InputFile multiFile = new InputFile.Builder()
                    .fileType(FileType.MULTI)
                    .filePath("input/demo")
                    .audios(List.of(
                            new Audio.Builder()
                                    .in(new Track(0))
                                    .remap(new Track(1))
                                    .build()))
                    .build();

            // when
            Throwable thrown = catchThrowable(() ->
                    new Input.Builder()
                            .storageId("storage-001")
                            .files(List.of(videoFile, multiFile))
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(INVALID_VALUE);
            assertThat(ex.getDetailMessage()).isEqualTo("The number of files with file_type 'multi' or 'video' must not exceed 1 when creating an Input.");
            logger.debug(mapper.writeValueAsString(ex));
        }
    }
}