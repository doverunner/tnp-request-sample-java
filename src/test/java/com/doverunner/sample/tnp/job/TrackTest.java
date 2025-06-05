package com.doverunner.sample.tnp.job;

import static com.doverunner.sample.tnp.exception.ErrorCode.OUT_OF_RANGE_VALUE;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TrackTest {

    private static final Logger logger = LoggerFactory.getLogger(TrackTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Track 생성 시 index 값이 음수라면 지정한 TnPCustomException이 발생해야 한다.")
    void constructor_with_negative_track_should_throws_exception() throws Exception {
        // given
        int negativeIndex = -1;

        // when
        Throwable thrown = catchThrowable(() -> new Track(negativeIndex));

        // then
        Assertions.assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        Assertions.assertThat(ex.getErrorCode()).isEqualTo(OUT_OF_RANGE_VALUE);
        Assertions.assertThat(ex.getMessage()).contains(OUT_OF_RANGE_VALUE.getCode(), OUT_OF_RANGE_VALUE.getMessage());
        Assertions.assertThat(ex.getDetailMessage()).isEqualTo("The track index must be greater than or equal to 0.");

        logger.debug(mapper.writeValueAsString(ex));
    }
}