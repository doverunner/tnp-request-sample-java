package com.pallycon.sample.tnp.constant.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ApiUrl {

    AUTH_TOKEN("https://tnp.pallycon.com/api/token/%s", HttpMethod.POST),
    CREATE_JOB("https://tnp.pallycon.com/api/job/%s", HttpMethod.POST),
    GET_JOB_LIST("https://tnp.pallycon.com/api/job/%s", HttpMethod.GET),
    GET_JOB_DETAIL("https://tnp.pallycon.com/api/job/%1$s/%2$s", HttpMethod.GET),
    STOP_JOB("https://tnp.pallycon.com/api/job/%1$s/%2$s/stop", HttpMethod.PUT),
    RESTART_JOB("https://tnp.pallycon.com/api/job/%1$s/%2$s/restart", HttpMethod.PUT);

    private final String urlPattern;
    private final HttpMethod method;

    ApiUrl(String url, HttpMethod method) {
        this.urlPattern = url;
        this.method = method;
    }

    public String getUrl(String... params) {
        int expectedParams = countFormatSpecifiers(urlPattern);
        int actualParams = (params == null) ? 0 : params.length;

        if (actualParams != expectedParams) {
            throw new IllegalArgumentException("URL 패턴에 필요한 파라미터 개수와 입력된 파라미터 개수가 다릅니다.\n"
                    + "필요 파라미터 : " + expectedParams + " 입력 파라미터 : " + actualParams);
        }

        return String.format(urlPattern, (Object[]) params);
    }

    private int countFormatSpecifiers(String pattern) {
        Pattern p = Pattern.compile("%(\\d+\\$)?s");
        Matcher m = p.matcher(pattern);
        int count = 0;

        while (m.find()) {
            count++;
        }

        return count;
    }

    public String getMethod() {
        return method.getMethod();
    }
}
