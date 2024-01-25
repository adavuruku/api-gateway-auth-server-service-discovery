package com.example.user_service.dtos.request;

import com.example.user_service.utils.ValidationLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class CreateCustomerDto {
    @NotNull(message="Name is mandatory", groups = {ValidationLevel.onCreate.class})
    @Size(min = 2, message = "Name must be atleast two characters long", groups = {ValidationLevel.onCreate.class, ValidationLevel.onUpdate.class})
    private String firstName;

    @NotNull(message="Name is mandatory", groups = {ValidationLevel.onCreate.class})
    @Size(min = 2, message = "Name must be atleast two characters long", groups = {ValidationLevel.onCreate.class, ValidationLevel.onUpdate.class})
    private String lastName;

    @NotNull(message = "Password is mandatory", groups = {ValidationLevel.onCreate.class, ValidationLevel.onPasswordChange.class, ValidationLevel.onAuthenticateUser.class})
    @Size(min = 8, message = "Password must be atleast 8 characters long", groups = {ValidationLevel.onCreate.class, ValidationLevel.onPasswordChange.class, ValidationLevel.onAuthenticateUser.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{2,}$", message = "Password must contain atleast One Special Character and a Capital Letter",
            groups = {ValidationLevel.onCreate.class, ValidationLevel.onPasswordChange.class, ValidationLevel.onAuthenticateUser.class})
//    @JsonIgnore
    private String password;

    @NotNull(message = "Password is mandatory", groups = { ValidationLevel.onPasswordChange.class})
    @Size(min = 8, message = "Password must be atleast 8 characters long", groups = {ValidationLevel.onPasswordChange.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{2,}$", message = "Password must contain atleast One Special Character and a Capital Letter",
            groups = {ValidationLevel.onPasswordChange.class})
//    @JsonIgnore
    private String newPassword;


    private String phoneNumber;

    private String profileImage;

    @Email(message = "Email Address is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            groups = {ValidationLevel.onCreate.class, ValidationLevel.onAdminUpdateRole.class,ValidationLevel.onPasswordChange.class})
    @NotEmpty(message = "Email Address cannot be empty",
            groups = {ValidationLevel.onCreate.class, ValidationLevel.onAdminUpdateRole.class, ValidationLevel.onPasswordChange.class})
    @NotNull(message="Email Address is mandatory", groups = {ValidationLevel.onCreate.class, ValidationLevel.onAdminUpdateRole.class, ValidationLevel.onPasswordChange.class})
    private String emailAddress;

    private String contactAddress;

    @NotNull(message="Specify if user is Admin", groups = {ValidationLevel.onAdminCreate.class, ValidationLevel.onAdminUpdateRole.class})
    @Pattern(regexp = "Y|N", message="Value is either Y or N", groups = {ValidationLevel.onAdminCreate.class, ValidationLevel.onAdminUpdateRole.class})
    private String isAdmin;

    @NotNull(message="Specify if user is a moderator", groups = {ValidationLevel.onAdminCreate.class, ValidationLevel.onAdminUpdateRole.class})
    @Pattern(regexp = "Y|N", message="Value is either Y or N", groups = {ValidationLevel.onAdminCreate.class, ValidationLevel.onAdminUpdateRole.class})
    private String isModerator;
}
