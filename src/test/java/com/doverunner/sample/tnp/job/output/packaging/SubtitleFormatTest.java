package com.doverunner.sample.tnp.job.output.packaging;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SubtitleFormatTest {

    private static final Logger logger = LoggerFactory.getLogger(SubtitleFormatTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("일부 필드만 주어져도 SubtitleFormat 객체 생성에 성공해야 한다.")
    void build_should_create_subtitle_format_when_partial_fields_are_given() throws Exception {
        // given
        String dash = "text.vtt";

        // when
        SubtitleFormat subtitleFormat = new SubtitleFormat.Builder()
                .dash(dash)
                .build();

        // then
        assertThat(subtitleFormat).isNotNull();
        logger.debug(mapper.writeValueAsString(subtitleFormat));
    }
}