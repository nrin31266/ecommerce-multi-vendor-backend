package com.vanrin05.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(String message) {
        super(message);
        this.errorCode = ErrorCode.DEFAULT_EXCEPTION;
    }


    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }


    private final ErrorCode errorCode;
}
