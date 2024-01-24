package com.example.user_service.dtos.response;



import com.example.user_service.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class CreateUserResponseDto extends AbstractResponseDto {
    private Users data;
}