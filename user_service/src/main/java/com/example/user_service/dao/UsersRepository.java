package com.example.user_service.dao;


import com.example.user_service.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends MongoRepository<Users, String>{

    Optional<Users> findByEmailAddress(String email);
    Optional<Users> findByEmailAddressAndIsCustomer(String email, String isCustomer);

    List<Users> findByIsAccountLockedIsAccountDeletedIsCustomer(boolean isAccountLocked, boolean isAccountDeleted, String isCustomer);

    List<Users> findByIsAccountLockedIsAccountDeletedIsModerator(boolean isAccountLocked, boolean isAccountDeleted, String isModerator);

    List<Users> findByIsAccountLockedIsAccountDeletedIsAdmin(boolean isAccountLocked, boolean isAccountDeleted, String isAdmin);

    Optional<Users> findByEmailAddressAndIsAccountDeleted(String emailAddress, boolean isAccountDeleted);
    Optional<Users> findByIdAndIsAccountDeleted(String id, boolean isAccountDeleted);

    Optional<Users> findByEmailAddressAndIsAccountDeletedAndIsAccountLocked(String emailAddress, boolean isAccountDeleted, boolean isAccountLocked);

//    Users findByUsernameAndPassword(String username, String password);

//    HashSet<Users> findByUsernameContaining(String username);

//    HashSet<Users> findByFullnameContaining(String fullname);

}
