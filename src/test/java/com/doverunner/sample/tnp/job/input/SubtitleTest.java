package com.doverunner.sample.tnp.job.input;

import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SubtitleTest {

    private static final Logger logger = LoggerFactory.getLogger(SubtitleTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("필수값이 모두 주어지면 Subtitle 객체 생성에 성공해야 한다.")
    void build_should_return_subtitle() throws Exception {
        // given
        String filePath = "input/subtitle/ko.vtt";
        String language = "ko";

        // when
        Subtitle subtitle = new Subtitle.Builder()
                .filePath(filePath)
                .language(language)
                .build();

        // then
        assertThat(subtitle).isNotNull();
        logger.debug(mapper.writeValueAsString(subtitle));
    }

    @Test
    @DisplayName("Subtitle 생성 시 filePath가 없다면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_subtitle_has_no_filepath() throws Exception {
        // given
        String language = "eng";

        // when
        Throwable thrown = catchThrowable(() ->
                new Subtitle.Builder()
                        .language(language)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("The 'filePath' field is required when creating a Subtitle.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("Subtitle 생성 시 language가 없다면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_subtitle_has_no_language() throws Exception {
        // given
        String filePath = "input/subtitle/ko.vtt";

        // when
        Throwable thrown = catchThrowable(() ->
                new Subtitle.Builder()
                        .filePath(filePath)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("The 'language' field is required when creating a Subtitle.");

        logger.debug(mapper.writeValueAsString(ex));
    }
}