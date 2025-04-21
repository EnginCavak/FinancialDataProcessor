package com.example.financial_data_processor_3.repository;

import com.example.financial_data_processor_3.model.Rate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RateCacheRepository {

    private final RedisTemplate<String, Rate> redisTemplate;

    public RateCacheRepository(RedisTemplate<String, Rate> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRaw(String key, Rate rate) {
        redisTemplate.opsForValue().set("raw:" + key, rate);
    }

    public Rate findRaw(String key) {
        return redisTemplate.opsForValue().get("raw:" + key);
    }
}
