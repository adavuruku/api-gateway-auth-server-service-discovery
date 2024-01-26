package com.example.user_service.dtos.response;



import com.example.user_service.model.Users;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class UserResponseDto extends AbstractResponseDto {
    private Users data;
}