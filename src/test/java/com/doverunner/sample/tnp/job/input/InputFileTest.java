package com.doverunner.sample.tnp.job.input;

import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.doverunner.sample.tnp.job.FileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.Track;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InputFileTest {

    private static final Logger logger = LoggerFactory.getLogger(InputFileTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("정상 생성 케이스")
    class ValidInputFileCreationTest {

        @Test
        @DisplayName("fileType이 video이고 audios가 없어도 InputFile 객체 생성에 성공해야 한다.")
        void build_should_return_input_file_when_video_without_audios() throws Exception {
            // given
            FileType fileType = FileType.VIDEO;
            String filePath = "input/video.mp4";

            // when
            InputFile inputFile = new InputFile.Builder()
                    .fileType(fileType)
                    .filePath(filePath)
                    .build();

            // then
            assertThat(inputFile).isNotNull();
            logger.debug(mapper.writeValueAsString(inputFile));
        }

        @Test
        @DisplayName("fileType이 multi이고 audios가 유효하면 InputFile 객체 생성에 성공해야 한다.")
        void build_should_return_input_file_when_multi_with_valid_audios() throws Exception {
            // given
            FileType fileType = FileType.MULTI;
            String filePath = "input/input.mp4";

            Audio audio = new Audio.Builder()
                    .in(new Track(0))
                    .remap(new Track(1))
                    .build();

            // when
            InputFile inputFile = new InputFile.Builder()
                    .fileType(fileType)
                    .filePath(filePath)
                    .audios(List.of(audio))
                    .build();

            assertThat(inputFile).isNotNull();
            logger.debug(mapper.writeValueAsString(inputFile));
        }
    }

    @Nested
    @DisplayName("예외 발생 케이스 - 조건 미충족")
    class InvalidConditionValidationTest {

        @Test
        @DisplayName("fileType이 multi인데 audios가 없으면 지정한 TnPCustomException이 발생해야 한다.")
        void build_should_throw_exception_when_fileType_is_multi_without_audios() throws Exception {
            // given
            FileType fileType = FileType.MULTI;
            String filePath = "input/input.mp4";

            // when
            Throwable thrown = catchThrowable(() ->
                    new InputFile.Builder()
                            .fileType(fileType)
                            .filePath(filePath)
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'audios' field is required when the fileType is 'audio' or 'multi'.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("fileType이 audio인데 audios가 비어있으면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_fileType_is_audio_and_audios_is_empty() throws Exception {
            // given
            FileType fileType = FileType.AUDIO;
            String filePath = "input/audio.aac";

            // when
            Throwable thrown = catchThrowable(() ->
                    new InputFile.Builder()
                            .fileType(fileType)
                            .filePath(filePath)
                            .build()
            );

            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'audios' field is required when the fileType is 'audio' or 'multi'.");

            logger.debug(mapper.writeValueAsString(ex));
        }
    }

    @Nested
    @DisplayName("필수 필드 누락 시 예외 발생 테스트")
    class RequiredFieldValidationTest {

        @Test
        @DisplayName("fileType이 null이면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_fileType_is_null() throws Exception {
            // given
            String filePath = "input/video.mp4";

            // when
            Throwable thrown = catchThrowable(() ->
                    new InputFile.Builder()
                            .filePath(filePath)
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'fileType' field is required when creating an InputFile.");

            logger.debug(mapper.writeValueAsString(ex));
        }

        @Test
        @DisplayName("filePath가 null이면 예외가 발생해야 한다.")
        void build_should_throw_exception_when_filePath_is_null() throws Exception {
            // given
            FileType fileType = FileType.VIDEO;

            // when
            Throwable thrown = catchThrowable(() ->
                    new InputFile.Builder()
                            .fileType(fileType)
                            .build()
            );

            // then
            assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
            TnpCustomException ex = (TnpCustomException) thrown;
            assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
            assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
            assertThat(ex.getDetailMessage()).isEqualTo("The 'filePath' field is required when creating an InputFile.");

            logger.debug(mapper.writeValueAsString(ex));
        }
    }
}