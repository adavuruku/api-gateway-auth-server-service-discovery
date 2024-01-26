package com.example.user_service.dtos.response;



import com.example.user_service.model.Users;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class UserListResponseDto extends AbstractResponseDto {
    private List<Users> data;
}