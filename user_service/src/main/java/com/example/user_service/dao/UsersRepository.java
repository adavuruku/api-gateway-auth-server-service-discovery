package com.example.user_service.dao;


import com.example.user_service.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;


@Repository
public interface UsersRepository extends MongoRepository<Users, String>{

    Optional<Users> findByEmailAddress(String email);

    Users findByUsernameAndPassword(String username, String password);

    HashSet<Users> findByUsernameContaining(String username);

    HashSet<Users> findByFullnameContaining(String fullname);

}
