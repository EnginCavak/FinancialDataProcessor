package com.example.financial_data_processor_3.integration;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RateKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public RateKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRawRate(String rateKey, double value) {
        kafkaTemplate.send("raw-rates", rateKey, String.valueOf(value));
    }
}
