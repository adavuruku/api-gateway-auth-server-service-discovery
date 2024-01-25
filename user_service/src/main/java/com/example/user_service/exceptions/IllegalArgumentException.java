package com.example.user_service.exceptions;

import com.example.user_service.exceptions.dto.ErrorCode;

import java.util.Map;

public class IllegalArgumentException extends BaseException {
    public IllegalArgumentException(String message, Map<String, String> errorMetaData) {
        this(ErrorCode.BAD_ARGUMENT,message, errorMetaData);
    }

    public IllegalArgumentException(String message) {
        this(ErrorCode.BAD_ARGUMENT,message, null);
    }



    public IllegalArgumentException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode,message, errorMetaData);
    }

    public IllegalArgumentException(String message, Map<String, String> errorMetaData, Throwable cause) {
        this(ErrorCode.BAD_ARGUMENT,message, errorMetaData, cause);
    }

    public IllegalArgumentException(ErrorCode errorCode,
                                    String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode,message, errorMetaData, cause);
    }
}
