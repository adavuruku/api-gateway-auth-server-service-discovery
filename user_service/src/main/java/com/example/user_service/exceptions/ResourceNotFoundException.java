package com.example.user_service.exceptions;

import com.example.user_service.exceptions.dto.ErrorCode;

import java.util.Map;

public class ResourceNotFoundException  extends BaseException {
    public ResourceNotFoundException(String message, Map<String, String> errorMetaData) {
        this(ErrorCode.RESOURCE_NOT_FOUND,message, errorMetaData);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode,message, errorMetaData);
    }

    public ResourceNotFoundException(String message, Map<String, String> errorMetaData, Throwable cause) {
        this(ErrorCode.RESOURCE_NOT_FOUND,message, errorMetaData, cause);
    }

    public ResourceNotFoundException(ErrorCode errorCode,
                                     String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode,message, errorMetaData, cause);
    }
}
