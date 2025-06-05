package com.doverunner.sample.tnp.exception;

public enum ErrorCode {

    MISSING_REQUIRED_VALUE("E001", "Required input value is missing."),
    OUT_OF_RANGE_VALUE("E002", "Input value is out of the allowed range."),
    INVALID_VALUE("E003", "Input value is invalid."),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}