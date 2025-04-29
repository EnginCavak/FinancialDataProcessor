package com.example.financial_data_processor_3.integration;

import com.example.financial_data_processor_3.model.RateFields;
import com.example.financial_data_processor_3.service.RateCalculator;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** platformX-raw konularını dinler, hesaplayıp yeni konuya yollar */
@Component
@RequiredArgsConstructor
public class RawRateKafkaConsumer {

    private final RateCalculator      calculator;
    private final RateKafkaProducer   producer;

    @KafkaListener(
            topics = {"platform1-raw", "platform2-raw"},
            groupId = "calc-service"
    )
    public void onMessage(ConsumerRecord<String, String> rec) {

        String pair  = rec.key();
        double price = Double.parseDouble(rec.value());

        RateFields fields = calculator.calculate(price);

        producer.sendCalculatedRate(pair, fields);
    }
}
