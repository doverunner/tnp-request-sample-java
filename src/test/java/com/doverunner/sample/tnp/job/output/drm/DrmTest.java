package com.doverunner.sample.tnp.job.output.drm;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrmTest {

    private static final Logger logger = LoggerFactory.getLogger(DrmTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Builder 패턴을 사용하여 Drm 객체 생성에 성공해야 한다.")
    void build_should_return_Drm() throws Exception {
        // given
        Boolean enabled = true;
        DrmOption option = new DrmOption.Builder()
                .multiKey(true)
                .maxSdHeight(480)
                .maxHdHeight(1080)
                .maxUhd1Height(2160)
                .skipAudioEncryption(false)
                .clearLead(5)
                .generateTrackTypeManifests(true)
                .build();

        // when
        Drm drm = new Drm.Builder()
                .enabled(enabled)
                .option(option)
                .build();

        // then
        assertThat(drm).isNotNull();
        logger.debug(mapper.writeValueAsString(drm));
    }
}