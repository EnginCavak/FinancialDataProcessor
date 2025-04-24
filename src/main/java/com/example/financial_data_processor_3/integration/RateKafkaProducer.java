package com.example.financial_data_processor_3.integration;

import com.example.financial_data_processor_3.model.Rate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RawRateKafkaConsumer {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CalculatedRateService calculatedRateService;

    public RawRateKafkaConsumer(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            CalculatedRateService calculatedRateService) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.calculatedRateService = calculatedRateService;
    }

    @KafkaListener(
            topics = {"platform1-raw", "platform2-raw"},
            groupId = "raw-rate-group"
    )
    public void listen(ConsumerRecord<String,String> record) {
        String pair = record.key();
        double value = Double.parseDouble(record.value());
        Rate rate = new Rate(pair, value);

        // Redis'e kaydet
        String key = "raw:" + pair;
        try {
            String json = objectMapper.writeValueAsString(rate);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            // hatayı logla
        }

        // Hesaplama servisini tetikle
        calculatedRateService.calculateAndSend(pair);
    }
}