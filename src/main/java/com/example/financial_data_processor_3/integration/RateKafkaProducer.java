package com.example.financial_data_processor_3.integration;

import com.example.financial_data_processor_3.model.RateFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka'ya ham ve hesaplanmış kurlar gönderir.
 */
@Component
public class RateKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(RateKafkaProducer.class);

    private final KafkaTemplate<String, String> rawTemplate;
    private final KafkaTemplate<String, Object> objTemplate;

    public RateKafkaProducer(KafkaTemplate<String, String> rawTemplate,
                             KafkaTemplate<String, Object> objTemplate) {
        this.rawTemplate = rawTemplate;
        this.objTemplate = objTemplate;
    }

    public void sendRawRate(String pair, double value) {
        log.info("Sending RAW rate: {} = {}", pair, value);
        rawTemplate.send("raw-rates", pair, String.valueOf(value));
    }

    public void sendCalculatedRate(String key, RateFields fields) {
        log.info("Sending CALCULATED rate for key={}", key);
        objTemplate.send("calculated-rates", key, fields);
    }
}
