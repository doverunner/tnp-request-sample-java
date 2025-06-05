package com.doverunner.sample.tnp.constant.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ApiUrl {

    AUTH_TOKEN("https://tnp.doverunner.com/api/token/%s", HttpMethod.POST),
    CREATE_JOB("https://tnp.doverunner.com/api/job/%s", HttpMethod.POST),
    GET_JOB_LIST("https://tnp.doverunner.com/api/job/%s", HttpMethod.GET),
    GET_JOB_DETAIL("https://tnp.doverunner.com/api/job/%1$s/%2$s", HttpMethod.GET),
    STOP_JOB("https://tnp.doverunner.com/api/job/%1$s/%2$s/stop", HttpMethod.PUT),
    RESTART_JOB("https://tnp.doverunner.com/api/job/%1$s/%2$s/restart", HttpMethod.PUT);

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
            throw new IllegalArgumentException(
                    "The number of parameters required by the URL pattern does not match the number of provided parameters.\n"
                            + "Expected parameters: " + expectedParams + ", Provided parameters: " + actualParams);
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
