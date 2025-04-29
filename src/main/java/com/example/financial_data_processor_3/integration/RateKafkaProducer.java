package com.example.financial_data_processor_3.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateKafkaProducer {

    private final KafkaTemplate<String, String> rawTemplate;
    private final KafkaTemplate<String, Object> objTemplate;

    /** Old signature – still used in several places */
    public void sendRawRate(String pair, double value) {
        rawTemplate.send("platform1-raw", pair, String.valueOf(value));
    }

    /** NEW overload: lets callers pass an explicit topic */
    public void sendRawRate(String topic, String pair, double value) {
        rawTemplate.send(topic, pair, String.valueOf(value));
    }

    public void sendCalculatedRate(String key, Object fields) {
        objTemplate.send("calculated-rates", key, fields);
    }
}
