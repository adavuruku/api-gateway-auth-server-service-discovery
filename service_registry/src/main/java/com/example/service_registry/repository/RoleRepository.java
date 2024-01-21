package com.example.service_registry.repository;

import com.example.service_registry.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;


public interface RoleRepository extends MongoRepository<Role, String> {
    List<Role> findByNameIn(List<String> name);
    public long count();

}