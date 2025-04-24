package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.subscriber.Subscriber;
import com.example.financial_data_processor_3.subscriber.SubscriberLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * Central Spring @Config class.
 * <p>
 * – Loads all Subscriber implementations listed in
 *   <code>src/main/resources/subscribers.properties</code><br>
 * – (You can add more beans here later: KafkaTemplate, RedisTemplate, etc.)
 */
@Configuration
public class AppConfig {

    @Bean
    public List<Subscriber> subscribers() throws IOException {
        // SubscriberLoader now reads subscribers.properties automatically.
        return SubscriberLoader.loadSubscribers();
    }

    /* --- placeholder for future beans ---
    @Bean
    public KafkaTemplate<String, byte[]> kafkaTemplate(...) { … }

    @Bean
    public RedisTemplate<String, Rate> redisTemplate(...) { … }
    -------------------------------------- */
}
