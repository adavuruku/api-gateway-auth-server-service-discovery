package com.example.user_service.exceptions;

import com.example.user_service.exceptions.dto.ErrorCode;

import java.util.Map;

public class WebServiceException extends BaseException {
    public WebServiceException(String message, Map<String, String> errorMetaData) {
        this(ErrorCode.INTERNAL_SERVER_ERROR,message, errorMetaData);
    }

    public WebServiceException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode,message, errorMetaData);
    }

    public WebServiceException(String message, Map<String, String> errorMetaData, Throwable cause) {
        this(ErrorCode.INTERNAL_SERVER_ERROR, message, errorMetaData, cause);
    }

    public WebServiceException(ErrorCode errorCode,
                               String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode,message, errorMetaData, cause);
    }
}