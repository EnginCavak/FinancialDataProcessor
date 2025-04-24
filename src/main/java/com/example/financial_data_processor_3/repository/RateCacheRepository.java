package com.example.financial_data_processor_3.repository;

import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.model.RateStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RateCacheRepository {

    private final RedisTemplate<String, Object> redis;

    public RateCacheRepository(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    /* ---------------- existing ---------------- */
    public void saveRaw(String rateName, Rate rate) {
        redis.opsForValue().set("raw:" + rateName, rate);
    }

    /* ---------------- NEW --------------------- */
    public void saveCalculated(String rateName, RateFields fields) {
        redis.opsForValue().set("calc:" + rateName, fields);
    }

    public void saveStatus(String rateName, RateStatus status) {
        redis.opsForValue().set("status:" + rateName, status);
    }
}
