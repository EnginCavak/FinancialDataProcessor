package com.example.financial_data_processor_3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.financial_data_processor_3.subscriber.SubscriberLoader;
import java.util.List;
import com.example.financial_data_processor_3.subscriber.Subscriber;

@Configuration
public class AppConfig {

    @Bean
    public List<Subscriber> subscribers() throws Exception {
        // subscriberLoader yazmıştık
        // "subscribers.properties" içinde class isimleri var
        return SubscriberLoader.loadSubscribers("/subscribers.properties");
    }

    // Kafka, Redis bean'leri burada da manuel tanımlanabilir.
}