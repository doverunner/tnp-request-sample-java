package com.doverunner.sample.tnp.job.output.transcoding;

import static com.doverunner.sample.tnp.exception.ErrorCode.INVALID_VALUE;
import static com.doverunner.sample.tnp.exception.ErrorCode.MISSING_REQUIRED_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.doverunner.sample.tnp.exception.TnpCustomException;
import com.doverunner.sample.tnp.job.FileType;
import com.doverunner.sample.tnp.job.Track;
import com.doverunner.sample.tnp.job.output.transcoding.VideoTranscoding.Builder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TranscodingTest {

    private static final Logger logger = LoggerFactory.getLogger(TranscodingTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private final String validTrackId = "video_1";
    private final FileType validTrackType = FileType.VIDEO;
    private final String validCodec = "h264";
    private final Integer validHeight = 1080;
    private final Integer validWidth = 1920;

    @Test
    @DisplayName("필수값이 모두 주어지면 Transcoding 객체 생성에 성공해야 한다.")
    void build_should_return_transcoding() throws Exception {
        // given
        BitrateMode bitrateMode = BitrateMode.VBR;
        Integer crf = 23;

        // when
        Transcoding transcoding = new Builder()
                .trackId(validTrackId)
                .trackType(validTrackType)
                .codec(validCodec)
                .height(validHeight)
                .width(validWidth)
                .bitrateMode(bitrateMode)
                .crf(crf)
                .build();

        // then
        assertThat(transcoding).isNotNull();
        logger.debug(mapper.writeValueAsString(transcoding));
    }

    @Test
    @DisplayName("bitrate_mode가 vbr이고 min_bitrate, max_bitrate만 설정된 경우 Transcoding 객체 생성에 성공해야 한다.")
    void build_should_return_transcoding_when_vbr_with_min_and_max() throws Exception {
        // given
        BitrateMode bitrateMode = BitrateMode.VBR;
        Integer minBitrate = 640;
        Integer maxBitrate = 1280;

        // when
        Transcoding transcoding = new Builder()
                .trackId(validTrackId)
                .trackType(validTrackType)
                .codec(validCodec)
                .height(validHeight)
                .width(validWidth)
                .bitrateMode(bitrateMode)
                .minBitrate(minBitrate)
                .maxBitrate(maxBitrate)
                .build();

        // then
        assertThat(transcoding).isNotNull();
        logger.debug(mapper.writeValueAsString(transcoding));
    }

    @Test
    @DisplayName("trackType이 audio인 경우 bypass로 검증을 거치지 않고 Transcoding 객체 생성에 성공해야 한다.")
    void build_should_return_transcoding_when_track_type_is_audio() throws Exception {
        // given
        String trackId = "audio_1";
        FileType trackType = FileType.AUDIO;
        String codec = "aac";
        List<Track> sources = List.of(new Track(1));


        // when
        Transcoding transcoding = new AudioTranscoding.Builder()
                .trackId(trackId)
                .trackType(trackType)
                .codec(codec)
                .sources(sources)
                .build();

        // then
        assertThat(transcoding).isNotNull();
        logger.debug(mapper.writeValueAsString(transcoding));
    }

    @Test
    @DisplayName("Transcoding 생성 시 trackId가 없으면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_trackId_missing() throws Exception {
        // given
        Integer bitrate = 1000;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .height(validHeight)
                        .width(validWidth)
                        .bitrate(bitrate)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getDetailMessage()).isEqualTo("The 'trackId' field is required when creating Transcoding.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("Transcoding 생성 시 video 타입인데 height가 없으면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_video_height_missing() throws Exception {
        // given
        Integer bitrate = 1000;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackId(validTrackId)
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .bitrate(bitrate)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getDetailMessage()).isEqualTo("The 'height' field is required when the trackType is 'video' during Transcoding creation.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("Transcoding 생성 시 height 혹은 width가 홀수라면 지정한 TnPCustomException이 발생해야 한다.")
    void build_should_throw_exception_when_height_or_width_is_odd_num() throws Exception {
        // given
        Integer bitrate = 1000;
        Integer oddHeight = 1081;
        Integer oddWidth = 1921;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackId(validTrackId)
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .bitrate(bitrate)
                        .height(oddHeight)
                        .width(oddWidth)
                        .build()
        );

        // then
        assertThat(thrown).isExactlyInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_VALUE);
        assertThat(ex.getDetailMessage()).isEqualTo("The 'height' value must be an even number when creating Transcoding.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("bitrate_mode가 vbr이고 bitrate, crf, min-bitrate,max-bitrate가 모두 없으면 예외가 발생해야 한다.")
    void build_should_throw_exception_when_vbr_with_nothing() throws Exception {
        // given
        BitrateMode bitrateMode = BitrateMode.VBR;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackId(validTrackId)
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .height(validHeight)
                        .width(validWidth)
                        .bitrateMode(bitrateMode)
                        .build()
        );

        // then
        assertThat(thrown).isInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(MISSING_REQUIRED_VALUE);
        assertThat(ex.getDetailMessage()).
                isEqualTo("When 'bitrate_mode' is VBR, one of the following must be provided: 'bitrate', 'min_bitrate and max_bitrate', or 'crf'.");

        logger.debug(mapper.writeValueAsString(thrown));
    }

    @Test
    @DisplayName("bitrate_mode가 vbr이고 bitrate와 crf를 동시에 설정하면 예외가 발생해야 한다.")
    void build_should_throw_exception_when_vbr_with_both_bitrate_and_crf() throws Exception {
        // given
        BitrateMode bitrateMode = BitrateMode.VBR;
        Integer bitrate = 1000;
        Integer crf = 23;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackId(validTrackId)
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .height(validHeight)
                        .width(validWidth)
                        .bitrateMode(bitrateMode)
                        .bitrate(bitrate)
                        .crf(crf)
                        .build()
        );

        // then
        assertThat(thrown).isInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_VALUE);
        assertThat(ex.getDetailMessage())
                .isEqualTo("When 'bitrate_mode' is VBR, only one of the following should be provided: 'bitrate', 'min_bitrate and max_bitrate', or 'crf'.");

        logger.debug(mapper.writeValueAsString(ex));
    }

    @Test
    @DisplayName("bitrate_mode가 vbr이고 min-bitrate만 설정되면 예외가 발생해야 한다.")
    void build_should_throw_exception_when_vbr_with_only_minBitrate() throws Exception {
        // given
        BitrateMode bitrateMode = BitrateMode.VBR;

        // when
        Throwable thrown = catchThrowable(() ->
                new Builder()
                        .trackId(validTrackId)
                        .trackType(validTrackType)
                        .codec(validCodec)
                        .height(validHeight)
                        .width(validWidth)
                        .bitrateMode(bitrateMode)
                        .minBitrate(1000)
                        .build()
        );

        // then
        assertThat(thrown).isInstanceOf(TnpCustomException.class);
        TnpCustomException ex = (TnpCustomException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_VALUE);
        assertThat(ex.getDetailMessage())
                .isEqualTo("When 'bitrate_mode' is VBR, both 'min_bitrate' and 'max_bitrate' must be set together.");

        logger.debug(mapper.writeValueAsString(ex));
    }
}