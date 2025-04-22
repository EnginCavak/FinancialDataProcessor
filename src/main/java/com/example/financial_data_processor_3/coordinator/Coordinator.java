package com.example.financial_data_processor_3.coordinator;

import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.model.RateStatus;
import com.example.financial_data_processor_3.repository.RateCacheRepository;
import org.springframework.stereotype.Component;

@Component
public class Coordinator {

    private final RateCacheRepository cacheRepo;
    private final RateKafkaProducer kafkaProducer;

    public Coordinator(RateCacheRepository cacheRepo, RateKafkaProducer kafkaProducer) {
        this.cacheRepo = cacheRepo;
        this.kafkaProducer = kafkaProducer;
    }

    public void onConnect(String platform, boolean status) {
        System.out.printf("[%s] Connection %s%n", platform, status ? "SUCCESS" : "FAIL");
    }

    public void onRateAvailable(String platform, String rateName, Rate rate) {
        cacheRepo.saveRaw(rateName, rate);
        kafkaProducer.sendRawRate(rateName, rate.getValue());
        System.out.println("Stored & published " + rate);
    }

    public void onDisconnect(String platform, boolean status) {
        System.out.printf("[%s] Disconnected: %s%n", platform, status);
    }

    // onDisconnect, onRateUpdate, onRateStatus ...
}
