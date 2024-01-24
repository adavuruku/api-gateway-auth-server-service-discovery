package com.example.user_service.dtos.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder(toBuilder = true)
public class AbstractResponseDto {

    private int code;
    private String message;

}
