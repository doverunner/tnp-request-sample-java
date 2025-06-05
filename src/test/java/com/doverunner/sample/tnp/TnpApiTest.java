package com.doverunner.sample.tnp;

import com.doverunner.sample.tnp.constant.web.ApiUrl;
import com.doverunner.sample.tnp.job.FileType;
import com.doverunner.sample.tnp.job.Job;
import com.doverunner.sample.tnp.job.Track;
import com.doverunner.sample.tnp.job.input.Audio;
import com.doverunner.sample.tnp.job.input.Input;
import com.doverunner.sample.tnp.job.input.InputFile;
import com.doverunner.sample.tnp.job.input.Subtitle;
import com.doverunner.sample.tnp.job.output.ForensicWatermarking;
import com.doverunner.sample.tnp.job.output.Output;
import com.doverunner.sample.tnp.job.output.drm.Drm;
import com.doverunner.sample.tnp.job.output.drm.DrmOption;
import com.doverunner.sample.tnp.job.output.packaging.Packaging;
import com.doverunner.sample.tnp.job.output.packaging.PackagingOption;
import com.doverunner.sample.tnp.job.output.packaging.SubtitleFormat;
import com.doverunner.sample.tnp.job.output.transcoding.AudioTranscoding;
import com.doverunner.sample.tnp.job.output.transcoding.BitrateMode;
import com.doverunner.sample.tnp.job.output.transcoding.Transcoding;
import com.doverunner.sample.tnp.job.output.transcoding.VideoTranscoding;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TnpApiTest {

    private static final Logger logger = LoggerFactory.getLogger(TnpApiTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    // NOTE: Please provide your actual SITE_ID, EMAIL_ID, ACCESS_KEY, and AUTH_TOKEN before executing the test cases.
    private static final String SITE_ID = "<Your SiteID>";
    private static final String EMAIL_ID = "<Your EmailID>";
    private static final String ACCESS_KEY = "<Your AccessKey>";
    private static final String AUTH_TOKEN = "<Bearer valid-token>"; // Set token value received from getAuthToken().

    @Test
    @DisplayName("Step 1: Request an authentication token using Basic Auth credentials")
    void getAuthToken() throws Exception {
        // given
        String input = EMAIL_ID + ":" + ACCESS_KEY;
        String encoded = Base64.getEncoder().encodeToString(input.getBytes());
        String url = ApiUrl.AUTH_TOKEN.getUrl(SITE_ID);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "basic " + encoded)
                .GET()
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // then
        Map<String, Object> responseMap = mapper.readValue(response.body(), new TypeReference<>() {});
        logger.debug("get AuthToken Response:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMap));
    }

    @Test
    @DisplayName("Step 2: Submit a job creation request with input and output details")
    void createJob() throws Exception {
        // given
        String jobName = "jobName";
        String contentId = "test-cid";
        Input input = buildInput();
        Output output = buildOutput();
        Job job = new Job.Builder()
                .jobName(jobName)
                .contentId(contentId)
                .input(input)
                .output(output)
                .build();
        logger.debug("[Job Request]");
        logger.debug("Request Body:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(job));

        String url = ApiUrl.CREATE_JOB.getUrl(SITE_ID);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", AUTH_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(job)))
                .build();

        // when
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // then
        Map<String, Object> responseMap = mapper.readValue(response.body(), new TypeReference<>() {});
        logger.debug("create Job Response:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMap));
    }

    private Input buildInput() {
        return new Input.Builder()
                .storageId("6a09c1414c3f434ea2271128bcc8ea36")
                .files(buildFiles())
                .subtitles(buildSubtitles())
                .build();
    }

    private List<InputFile> buildFiles() {
        return List.of(
                new InputFile.Builder()
                        .fileType(FileType.MULTI)
                        .filePath("input/input-file1.mp4")
                        .audios(buildAudios())
                        .build()
        );
    }

    private List<Audio> buildAudios() {
        return List.of(
                new Audio.Builder()
                        .in(new Track(0))
                        .remap(new Track(0))
                        .build()
        );
    }

    private List<Subtitle> buildSubtitles() {
        return List.of(
                new Subtitle.Builder()
                        .filePath("input/subtitle/en.vtt")
                        .language("eng")
                        .build()
        );
    }

    private Output buildOutput() {
        return new Output.Builder()
                .storageId("output-storage-id")
                .path("output")
                .transcodings(buildTranscodings())
                .packaging(buildPackaging())
                .drm(buildDrm())
                .forensicWatermarking(new ForensicWatermarking(true))
                .build();
    }

    private List<Transcoding> buildTranscodings() {
        return List.of(
                new VideoTranscoding.Builder()
                        .trackId("video1")
                        .trackType(FileType.VIDEO)
                        .trackName("video")
                        .codec("h264")
                        .height(1080)
                        .width(1920)
                        .bitrateMode(BitrateMode.CBR)
                        .bitrate(10000)
                        .build(),
                new AudioTranscoding.Builder()
                        .trackId("audio1")
                        .trackType(FileType.AUDIO)
                        .trackName("english")
                        .codec("aac")
                        .language("en")
                        .sources(List.of(new Track(0)))
                        .build()
        );
    }

    private Packaging buildPackaging() {
        return new Packaging.Builder()
                .dash(true)
                .hls(false)
                .cmaf(false)
                .option(new PackagingOption.Builder()
                        .minBufferTime(2)
                        .enableAverageBandwidthMpd(false)
                        .subtitleFormat(new SubtitleFormat.Builder()
                                .dash("text_vtt")
                                .build())
                        .build())
                .build();
    }

    private Drm buildDrm() {
        return new Drm.Builder()
                .enabled(true)
                .option(new DrmOption.Builder()
                        .multiKey(false)
                        .maxSdHeight(0)
                        .maxHdHeight(0)
                        .maxUhd1Height(0)
                        .skipAudioEncryption(false)
                        .clearLead(0)
                        .generateTrackTypeManifests(false)
                        .build())
                .build();
    }
}
