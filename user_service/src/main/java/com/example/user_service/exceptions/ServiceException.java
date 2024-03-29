package com.example.user_service.exceptions;

import com.example.user_service.exceptions.dto.ErrorCode;

import java.util.Map;

public class ServiceException extends BaseException {

    public ServiceException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode, message, errorMetaData);
    }

    public ServiceException(
            ErrorCode errorCode, String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode, message, errorMetaData, cause);
    }
}