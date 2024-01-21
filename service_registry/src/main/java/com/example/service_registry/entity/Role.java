package com.example.service_registry.entity;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("roles")
@Data
@Builder
public class Role {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
}
