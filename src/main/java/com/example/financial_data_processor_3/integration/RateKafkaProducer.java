package com.example.financial_data_processor_3.integration;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RateKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public RateKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRawRate(String rateData) {
        // "raw-rates" topic’e gönderelim
        kafkaTemplate.send("raw-rates", rateData);
    }

    public void sendCalculatedRate(String rateData) {
        kafkaTemplate.send("calculated-rates", rateData);
    }
}
