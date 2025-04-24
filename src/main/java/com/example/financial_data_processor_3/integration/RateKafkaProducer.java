package com.example.financial_data_processor_3.integration;
import com.example.financial_data_processor_3.service.RateCalculator;
import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.coordinator.Coordinator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import com.example.financial_data_processor_3.model.RateFields;
import lombok.RequiredArgsConstructor;                     // Lombok varsa
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor                                   // Lombok yoksa ↓ ctor’u elle ekle
public class RateKafkaProducer {

    /* -------------------------------------------------
       İki ayrı KafkaTemplate:
         • rawTemplate  ->  String  key / String value
         • objTemplate  ->  String  key / Object value
       ------------------------------------------------- */
    private final KafkaTemplate<String, String> rawTemplate;
    private final KafkaTemplate<String, Object> objTemplate;

    /* ------------ HAM KUR --------------------------- */
    public void sendRawRate(String pair, double value) {
        rawTemplate.send("raw-rates", pair, String.valueOf(value));
    }

    /* ------------ HESAPLANMIŞ KUR ------------------- */
    public void sendCalculatedRate(String key, RateFields fields) {
        objTemplate.send("calculated-rates", key, fields);
    }
}

/*  Lombok KULLANMIYORSAN constructor’ı kendin ekle:
public RateKafkaProducer(KafkaTemplate<String, String> rawTemplate,
                         KafkaTemplate<String, Object> objTemplate) {
    this.rawTemplate = rawTemplate;
    this.objTemplate = objTemplate;
}
*/
