package com.example.user_service.dtos.request;

import lombok.Data;

@Data
public class JWTDto {

    private String username;
    private String password;
}