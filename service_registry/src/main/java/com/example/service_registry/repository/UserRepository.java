package com.example.service_registry.repository;

import java.util.Optional;

import com.example.service_registry.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserNameOrEmail(String userName, String email);
//    User findByUserNameOrEmail(String id, String bookSlug);
//        List<BookSchema> findAll();
//        Page<BookSchema> findByisDeleted(boolean isDeleted, Pageable pageable);
//
//        Optional<BookSchema> findByBookSlug(String bookSlug);
//        Optional<BookSchema> findById(String id);
//        Optional<BookSchema> findByIdOrBookSlug(String id, String bookSlug);
//        Optional<BookSchema> findByIdAndIsDeleted(String id, boolean isDeleted);
//        Optional<BookSchema> findByIdAndIsDeletedAndCreatedBy(String id, boolean isDeleted,String createdBy);


public long count();

        }