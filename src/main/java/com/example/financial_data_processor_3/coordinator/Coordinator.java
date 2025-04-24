package com.example.financial_data_processor_3.coordinator;
import com.example.financial_data_processor_3.service.RateCalculator;
import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.coordinator.Coordinator;

import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.model.RateStatus;
import com.example.financial_data_processor_3.repository.RateCacheRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Central hub that receives events from Subscriber modules
 * and forwards them to cache, Kafka, etc.
 *
 * NOTE: there is **no Spring stereotype** here (@Component / @Service).
 * The bean is supplied explicitly in DemoRunnerConfig.
 */

/**
 * Koordinasyonu sağlar: raw price üretir ve Kafka'ya gönderir.
 */
public class Coordinator {

    private final RateCacheRepository cacheRepo;
    private final RateKafkaProducer   kafkaProducer;

    /* ------------------------------------------------------------------ */
    /*  Constructor                                                       */
    /* ------------------------------------------------------------------ */
    public Coordinator(RateCacheRepository cacheRepo,
                       RateKafkaProducer kafkaProducer) {
        this.cacheRepo     = cacheRepo;
        this.kafkaProducer = kafkaProducer;
    }

    /* ------------------------------------------------------------------ */
    /*  Call-backs from Subscribers                                       */
    /* ------------------------------------------------------------------ */
    public void onConnect(String platform, boolean success) {
        System.out.printf("[%s] Connection %s%n",
                platform, success ? "SUCCESS" : "FAIL");
    }

    public void onDisconnect(String platform, boolean success) {
        System.out.printf("[%s] Disconnected: %s%n", platform, success);
    }

    public void onRateAvailable(String platform,
                                String rateName,
                                Rate rate) {

        // 1) Cache raw quote
        cacheRepo.saveRaw(rateName, rate);

        // 2) Publish to Kafka
        kafkaProducer.sendRawRate(rateName, rate.getValue());

        System.out.printf("[%s] Stored & published %s=%f%n",
                platform, rateName, rate.getValue());
    }

    public void onRateUpdate(String platform,
                             String rateName,
                             RateFields fields) {

        cacheRepo.saveCalculated(rateName, fields);
        kafkaProducer.sendCalculatedRate(rateName, fields);
        System.out.printf("[%s] Updated %s -> %s%n",
                platform, rateName, fields);
    }

    public void onRateStatus(String platform,
                             String rateName,
                             RateStatus status) {

        cacheRepo.saveStatus(rateName, status);
        System.out.printf("[%s] Status %s -> %s%n",
                platform, rateName, status);
    }
}
