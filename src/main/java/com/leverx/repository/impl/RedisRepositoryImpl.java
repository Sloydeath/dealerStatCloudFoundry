package com.leverx.repository.impl;

import com.leverx.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Repository layer
 *
 * @author Andrew Panas
 */

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final Logger log = LoggerFactory.getLogger(RedisRepositoryImpl.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setHashcode(String email, String hashcode) {
        redisTemplate.opsForValue().set(email, hashcode);
        redisTemplate.expire(email, 24, TimeUnit.HOURS);
    }

    @Override
    public Object getHashcode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    @Override
    public void deleteHashcode(String email) {
        redisTemplate.delete(email);
    }

    @Override
    public boolean isExists(String email) {
        String code = (String) redisTemplate.opsForValue().get(email);
        return code != null && !code.isEmpty();
    }
}
