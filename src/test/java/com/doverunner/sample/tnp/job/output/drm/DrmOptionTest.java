package com.doverunner.sample.tnp.job.output.drm;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrmOptionTest {

    private static final Logger logger = LoggerFactory.getLogger(DrmOptionTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Builder 패턴을 사용하여 DrmOption 객체 생성에 성공해야 한다.")
    void build_should_return_DrmOption() throws Exception {
        // given
        Boolean multiKey = true;
        Integer maxSdHeight = 360;
        Integer maxHdHeight = 720;
        Integer maxUhd1Height = 2160;
        Boolean skipAudioEncryption = true;
        Integer clearLead = 5;
        Boolean generateTrackTypeManifests = true;

        // when
        DrmOption drmOption = new DrmOption.Builder()
                .multiKey(multiKey)
                .maxSdHeight(maxSdHeight)
                .maxHdHeight(maxHdHeight)
                .maxUhd1Height(maxUhd1Height)
                .skipAudioEncryption(skipAudioEncryption)
                .clearLead(clearLead)
                .generateTrackTypeManifests(generateTrackTypeManifests)
                .build();

        // then
        assertThat(drmOption).isNotNull();
        logger.debug(mapper.writeValueAsString(drmOption));
    }
}