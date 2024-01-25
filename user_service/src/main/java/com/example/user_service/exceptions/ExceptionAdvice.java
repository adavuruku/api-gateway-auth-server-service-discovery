package com.example.user_service.exceptions;

import com.example.user_service.exceptions.dto.ErrorCode;
import com.example.user_service.exceptions.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler{
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> onConstraintValidationException(
            ConstraintViolationException e) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ErrorCode.BAD_ARGUMENT);
        error.setMessage(e.getMessage());
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getDetails().put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//    @Override
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ErrorCode.BAD_ARGUMENT);
        error.setMessage(e.getMessage());
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getDetails().put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        response.setMessage(e.getMessage());
        response.setDetails(Map.of());
        return ResponseEntity.internalServerError().body(response);
    }
    @ExceptionHandler(WebServiceException.class)
    public ResponseEntity<ErrorResponse>  handleWebServiceException(WebServiceException e) {
        HttpStatus httpStatus = e.getErrorCode().getHttpStatus();
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        response.setMessage(e.getMessage());
        response.setDetails(e.getErrorMetaData());
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePasswordMissMatchException(PasswordMissMatchException exception) {
        var errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCode.BAD_ARGUMENT);
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setDetails(Map.of());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        var errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCode.INVALID_OPERATION);
        log.info(exception.getMessage());
        errorResponse.setMessage("Invalid password or email address");
//        errorResponse.setDetails(Map.of("cause", cause.getCause().toString()));
        errorResponse.setDetails(Map.of("credentials", exception.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.RESOURCE_NOT_FOUND);
        response.setMessage(e.getMessage());
        response.setDetails(Map.of("fields", e.getArgs().toString()));
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RemoteServerException.class)
    public ResponseEntity<ErrorResponse> handleRemoteServerException(RemoteServerException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        response.setMessage(e.getMessage());
        response.setDetails(Map.of("fields", e.getArgs().toString()));
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.BAD_ARGUMENT);
        response.setMessage(e.getMessage());
        response.setDetails(Map.of("fields", e.getErrorMetaData().toString()));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.CONFLICT);
        response.setMessage(e.getMessage());
        response.setDetails(Map.of("fields", e.getErrorMetaData().toString()));
        return ResponseEntity.status(status).body(response);
    }
}
