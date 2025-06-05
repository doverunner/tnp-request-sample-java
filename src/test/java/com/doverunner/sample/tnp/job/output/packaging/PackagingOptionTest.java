package com.doverunner.sample.tnp.job.output.packaging;

import static com.doverunner.sample.tnp.exception.ErrorCode.OUT_OF_RANGE_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doverunner.sample.tnp.exception.TnpCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PackagingOptionTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(PackagingOptionTest.class);

    @Test
    @DisplayName("일부 필드만 주어져도 PackagingOption 객체 생성에 성공해야 한다.")
    void build_should_create_packaging_option_when_partial_fields_are_given() throws Exception {
        // given
        Integer minBufferTime = 3;
        Boolean enableAverageBandwidthMpd = false;

        // when
        PackagingOption packagingOption = new PackagingOption.Builder()
                .minBufferTime(minBufferTime)
                .enableAverageBandwidthMpd(enableAverageBandwidthMpd)
                .build();

        // then
        assertThat(packagingOption).isNotNull();
        logger.debug(mapper.writeValueAsString(packagingOption));
    }

    @Test
    @DisplayName("PackagingOption 생성 시 minBufferTime이 2보다 적다면 지정한 예외가 발생해야 한다.")
    void build_should_throw_exception_when_min_buffer_time_is_less_than_2() throws Exception {
        // given
        Integer minBufferTime = 1;
        Boolean enableAverageBandwidthMpd = false;

        // when
        Throwable thrown = catchThrowable(() -> {
                    new PackagingOption.Builder()
                            .minBufferTime(minBufferTime)
                            .enableAverageBandwidthMpd(enableAverageBandwidthMpd)
                            .build();
        });

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(OUT_OF_RANGE_VALUE);
        assertThat(ex.getMessage()).contains(OUT_OF_RANGE_VALUE.getCode(), OUT_OF_RANGE_VALUE.getMessage());

        logger.debug(mapper.writeValueAsString(ex));
    }
}