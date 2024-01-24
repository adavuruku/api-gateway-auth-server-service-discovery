package com.example.auth_service.services;



import com.example.auth_service.dao.UsersRepository;
import com.example.auth_service.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;


    @Cacheable(value="user", key = "#emailAddress", unless="#result == null")
    public Optional<Users> getByUsrName(String emailAddress) {
        log.info("emaily: {}", emailAddress);
        log.info("res {}", usersRepository.findByEmailAddress(emailAddress));
        return usersRepository.findByEmailAddress(emailAddress);
    }

}