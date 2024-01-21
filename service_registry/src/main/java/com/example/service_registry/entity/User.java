package com.example.service_registry.entity;

import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.util.Set;

@Document("users")
@Data
@Builder
public class User {
    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String userName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Set<Role> roles;
}
