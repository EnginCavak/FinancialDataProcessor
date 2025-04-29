package com.example.financial_data_processor_3.coordinator;

import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.model.RateStatus;
import com.example.financial_data_processor_3.repository.RateCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Central hub between the REST layer, subscribers and Kafka.
 */
@Service
@RequiredArgsConstructor
public class Coordinator {

    private final RateCacheRepository cacheRepo;
    private final RateKafkaProducer   producer;

    /* ------------------------------------------------------------------
       High-level helper used by controllers
       ------------------------------------------------------------------ */
    public void publishRaw(String platform, String pair, double price) {
        String topic = platform + "-raw";                // e.g. platform1-raw
        producer.sendRawRate(topic, pair, price);
    }

    /* ------------------------------------------------------------------
       Callbacks expected by the Subscriber classes
       (they can be fleshed out later; for now we just log / stub)
       ------------------------------------------------------------------ */
    public void onConnect(String platform, boolean success) {
        System.out.printf("[%s] connection %s%n",
                platform, success ? "OK" : "FAILED");
    }

    public void onDisconnect(String platform, boolean success) {
        System.out.printf("[%s] disconnected (%s)%n", platform, success);
    }

    public void onRateAvailable(String platform,
                                String pair,
                                Rate rate) {

        cacheRepo.saveRaw(pair, rate);                   // cache
        producer.sendRawRate(pair, rate.getValue());     // kafka (old sig!)
        System.out.printf("[%s] raw %s=%f%n",
                platform, pair, rate.getValue());
    }

    public void onRateUpdate(String platform,
                             String pair,
                             RateFields fields) {

        cacheRepo.saveCalculated(pair, fields);
        producer.sendCalculatedRate(pair, fields);
        System.out.printf("[%s] calc-update %s -> %s%n",
                platform, pair, fields);
    }

    public void onRateStatus(String platform,
                             String pair,
                             RateStatus status) {

        cacheRepo.saveStatus(pair, status);
        System.out.printf("[%s] status %s -> %s%n",
                platform, pair, status);
    }
}
