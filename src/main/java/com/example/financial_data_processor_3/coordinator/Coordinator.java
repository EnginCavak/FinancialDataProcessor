package com.example.financial_data_processor_3.coordinator;

import com.example.financial_data_processor_3.model.Rate;
import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.model.RateStatus;
import com.example.financial_data_processor_3.repository.RateCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class Coordinator {

    @Autowired
    private RateCacheRepository rateCacheRepository;




    public void onConnect(String platformName, boolean status) {
        System.out.println("[" + platformName + "] Connection " + (status ? "SUCCESS" : "FAIL"));
        // Eğer bağlantı başarılıysa logla, vb.
    }

    public void onDisconnect(String platformName, boolean status) {
        System.out.println("[" + platformName + "] Disconnected: " + status);
    }

    public void onRateAvailable(String platformName, String rateName, Rate rate) {
        System.out.println("Rate available: " + rateName + " -> " + rate);
        // Cache kaydet
        rateCacheRepository.saveRawRate(rateName, rate);
        // Kafka publish
        rateKafkaProducer.sendRawRate(rateName + ":" + rate.getValue());
    }


    public void onRateUpdate(String platformName, String rateName, RateFields rateFields) {
        System.out.println("Rate update: " + rateName + " -> " + rateFields);
        // Güncellenmiş veriyi cache’e, DB’ye kaydet...
    }

    public void onRateStatus(String platformName, String rateName, RateStatus rateStatus) {
        System.out.println("Rate status changed: " + rateName + " -> " + rateStatus);
    }
}
