package com.example.service_registry.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String email;
    private String fullName;
    private String userName;
    private String password;
    private List<String> roles;

}
