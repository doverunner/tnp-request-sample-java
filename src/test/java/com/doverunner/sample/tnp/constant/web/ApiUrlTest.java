package com.doverunner.sample.tnp.constant.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiUrlTest {

    @Test
    @DisplayName("하나의 매개변수를 가진 URL을 반환해야 한다.")
    void getUrl_should_return_with_single_param() {
        // given
        String siteId = "TEST";

        // when
        String actual = ApiUrl.AUTH_TOKEN.getUrl(siteId);
        String expected = "https://tnp.doverunner.com/api/token/TEST";

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("하나 이상의 매개변수를 가진 URL을 반환해야 한다.")
    void getUrl_should_return_with_multi_param() {
        // given
        String siteId = "TEST";
        String jobId = "777";

        // when
        String actual = ApiUrl.GET_JOB_DETAIL.getUrl(siteId, jobId);
        String expected = "https://tnp.doverunner.com/api/job/TEST/777";

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("파라미터 순서에 따라 URL이 다르게 생성되어야 한다.")
    void getUrl_should_return_different_when_param_order_changes() {
        // given
        String siteId = "TEST";
        String jobId = "777";

        // when
        String url1 = ApiUrl.GET_JOB_DETAIL.getUrl(siteId, jobId);
        String url2 = ApiUrl.GET_JOB_DETAIL.getUrl(jobId, siteId);

        // then
        assertThat(url1).isEqualTo("https://tnp.doverunner.com/api/job/TEST/777");
        assertThat(url2).isEqualTo("https://tnp.doverunner.com/api/job/777/TEST");
        assertThat(url1).isNotEqualTo(url2);
    }

    @Test
    @DisplayName("URL의 파라미터 개수가 부족하면 예외가 발생해야 한다.")
    void getUrl_should_throw_exception_when_param_count_is_invalid() {
        // given
        String siteId = "TEST";

        // when & then
        assertThatThrownBy(() -> ApiUrl.GET_JOB_DETAIL.getUrl(siteId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The number of parameters required by the URL pattern does not match the number of provided parameters.");
    }
}