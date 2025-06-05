package com.doverunner.sample.tnp.job.input;

import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AudioTest {

    private static final Logger logger = LoggerFactory.getLogger(AudioTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("필수값이 모두 주어지면 Audio 객체 생성에 성공해야 한다.")
    void build_should_return_audio_mapping() throws Exception {
        // given
        Track in = new Track(0);
        Track remap = new Track(1);

        // when
        Audio audio = new Audio.Builder()
                .in(in)
                .remap(remap)
                .build();

        // then
        assertThat(audio).isNotNull();
        logger.debug(mapper.writeValueAsString(audio));
    }

    @Test
    @DisplayName("Audio 생성 시 in이 없다면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_audio_mapping_has_no_in() throws Exception {
        // given
        Track remap = new Track(1);

        // when
        Throwable thrown = catchThrowable(() ->
                new Audio.Builder()
                        .remap(remap)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("The 'in' field is required when creating an Audio.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("Audio 생성 시 remap이 없다면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_audio_mapping_has_no_remap() throws Exception {
        // given
        Track in = new Track(0);

        // when
        Throwable thrown = catchThrowable(() ->
                new Audio.Builder()
                        .in(in)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("The 'remap' field is required when creating an Audio.");

        logger.debug(mapper.writeValueAsString(ex));
    }
}