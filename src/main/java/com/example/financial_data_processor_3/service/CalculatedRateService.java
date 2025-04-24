package com.example.financial_data_processor_3.service;

import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CalculatedRateService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final RateKafkaProducer producer;

    public CalculatedRateService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            RateKafkaProducer producer) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.producer = producer;
    }

    public void calculateAndSend(String pair) {
        String key = "raw:" + pair;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return;

        try {
            // Ham Rate objesine dönüştür
            Rate rawRate = objectMapper.readValue(json, Rate.class);
            double mid = rawRate.getValue();
            double bid = mid - 0.0001;
            double ask = mid + 0.0001;

            // Hesaplanmış alanları hazırla
            RateFields fields = new RateFields();
            fields.setBid(bid);
            fields.setAsk(ask);
            fields.setMid((bid + ask) / 2);

            // Kafka’ya yolla
            producer.sendCalculatedRate(pair, fields);

            // Redis’e de kaydetmek isterseniz:
            redisTemplate.opsForValue().set("calc:" + pair,
                    objectMapper.writeValueAsString(fields));

        } catch (Exception e) {
            // log hata
        }
    }
}