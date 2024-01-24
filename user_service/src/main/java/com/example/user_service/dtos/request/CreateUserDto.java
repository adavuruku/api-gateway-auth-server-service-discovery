package com.example.user_service.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class CreateUserDto {
    @NotNull
    private String firstName;

    @Field(name = "LastName")
    @NotNull
    private String lastName;

    @Field(name = "PhoneNumber")
    private String phoneNumber;

    @Field(name = "Password")
    @NotNull
    @JsonIgnore
    private String password;

    @Field(name = "ProfileImage")
    private String profileImage;

    @Indexed(unique = true)
    @Field(name = "EmailAddress")
    @NotNull
    private String emailAddress;

    @Field(name = "ContactAddress")
    private String contactAddress;

    @NotNull
    @Field(name = "Timestamp")
    private Date timestamp = new Date();

    @NotNull
    @Field
    private Date createdOn;

    @NotNull
    @Field(name = "isAdminUser")
    private String isAdmin = "N";

    @NotNull
    @Field(name = "isModeratorUser")
    private String isModerator = "N";

    @NotNull
    @Field(name = "isCustomerUser")
    private String isCustomerUser = "Y";

    @Field(name = "isAccountDeleted")
    private boolean isAccountDeleted;

    @Field(name = "isAccountLocked")
    private boolean isAccountLocked;
}
