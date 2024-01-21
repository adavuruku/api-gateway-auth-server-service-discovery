package com.example.auth_service.services.redis;


import com.example.auth_service.dao.redis.TokensRedisRepository;
import com.example.auth_service.model.redis.TokensEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokensRedisService {


    @Autowired
    private TokensRedisRepository tokensRedisRepository;

    public TokensEntity save(TokensEntity entity) {
        return tokensRedisRepository.save(entity);
    }


    public Optional<TokensEntity> findById(String id) {
        return tokensRedisRepository.findById(id);
    }

    public Iterable<TokensEntity> findAll() {
        return tokensRedisRepository.findAll();
    }


}