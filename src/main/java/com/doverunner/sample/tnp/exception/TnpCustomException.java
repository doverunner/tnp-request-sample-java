package com.doverunner.sample.tnp.exception;

public class TnpCustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    public TnpCustomException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public TnpCustomException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return String.format("[%s] %s%s",
                errorCode.getCode(),
                errorCode.getMessage(),
                detailMessage != null ? " - " + detailMessage : ""
        );
    }
}
