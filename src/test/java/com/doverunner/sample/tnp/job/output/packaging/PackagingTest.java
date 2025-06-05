package com.doverunner.sample.tnp.job.output.packaging;

import static com.doverunner.sample.tnp.exception.ErrorCode.INVALID_VALUE;
import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PackagingTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(PackagingTest.class);

    @Test
    @DisplayName("필수값이 주어지면 Packaging 객체 생성에 성공해야 한다.")
    void build_should_return_packaging() throws Exception {
        // given
        Boolean dash = true;

        // when
        Packaging packaging = new Packaging.Builder()
                .dash(dash)
                .build();

        // then
        assertThat(packaging).isNotNull();
        logger.debug(mapper.writeValueAsString(packaging));
    }

    @Test
    @DisplayName("cmaf값이 true인 경우 Packaging 객체 생성에 성공해야 한다.")
    void build_should_return_packaging_when_only_cmaf_is_true() throws Exception {
        // given
        Boolean cmaf = true;

        // when
        Packaging packaging = new Packaging.Builder()
                .cmaf(cmaf)
                .build();

        // then
        assertThat(packaging).isNotNull();
        logger.debug(mapper.writeValueAsString(packaging));
    }

    @Test
    @DisplayName("Packaging 생성 시 dash / hls / cmaf 중 값이 없거나 false인 경우 지정한 예외를 발생해야 한다.")
    void build_should_throw_when_all_packaging_types_are_null_or_false() throws Exception {
        // given
        Boolean dash = false;
        Boolean cmaf = false;

        // when
        Throwable thrown = catchThrowable(() ->
                new Packaging.Builder()
                        .dash(dash)
                        .cmaf(cmaf)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getMessage()).contains(MISSING_REQUIRED_VALUE.getCode(), MISSING_REQUIRED_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("At least one of 'dash', 'hls', or 'cmaf' must be true when creating Packaging.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("Packaging 생성 시 cmaf 값이 true인 경우 dash 값도 true라면 지정한 예외를 발생해야 한다.")
    void build_should_throw_exception_when_cmaf_and_dash_or_hls_are_true() throws Exception {
        // given
        Boolean dash = true;
        Boolean cmaf = true;

        // when
        Throwable thrown = catchThrowable(() ->
                new Packaging.Builder()
                        .dash(dash)
                        .cmaf(cmaf)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_VALUE);
        assertThat(ex.getMessage()).contains(INVALID_VALUE.getCode(), INVALID_VALUE.getMessage());
        assertThat(ex.getDetailMessage()).isEqualTo("If 'cmaf' is true, both 'dash' and 'hls' must be false or null when creating Packaging.");

        logger.debug(mapper.writeValueAsString(ex));
    }
}
