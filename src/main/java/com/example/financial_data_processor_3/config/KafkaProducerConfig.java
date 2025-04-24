package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.model.RateFields;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    /* -------------- Ortak Ayarlar ------------------ */
    private Map<String, Object> baseProps() {
        Map<String, Object> m = new HashMap<>();
        m.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        m.put(ProducerConfig.ACKS_CONFIG, "all");
        m.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        m.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        m.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return m;
    }

    /* -------------- String / String ---------------- */
    @Bean
    public ProducerFactory<String, String> rawProducerFactory() {
        Map<String, Object> props = new HashMap<>(baseProps());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> rawTemplate() {
        return new KafkaTemplate<>(rawProducerFactory());
    }

    /* -------------- String / JSON(POJO) ------------ */
    @Bean
    public ProducerFactory<String, RateFields> calcProducerFactory() {
        Map<String, Object> props = new HashMap<>(baseProps());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, RateFields> calcTemplate() {
        return new KafkaTemplate<>(calcProducerFactory());
    }
}
