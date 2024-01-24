package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Document(collection="users")
@Data
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
//@Setter
//@Getter
public class Users implements Serializable {

    @Id
    private String id;
    @Field(name = "FirstName")
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
    private Date createdOn= new Date();

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
    private boolean isAccountDeleted = false;

    @Field(name = "isAccountLocked")
    private boolean isAccountLocked = false;

    @Override
    public String toString()
    {
        if(isAdmin!=null && isAdmin.equalsIgnoreCase("Y")) {
            return "User [id=" + id + ", uname=" + emailAddress + ", Admin User]";
        } else {
            return "User [id=" + id + ", uname=" + emailAddress + "]";
        }
    }

}
