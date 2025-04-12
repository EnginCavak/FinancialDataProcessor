package com.example.financial_data_processor_3;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.subscriber.Subscriber;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DemoRunnerConfig {

    @Bean
    public CommandLineRunner demoRunner(List<Subscriber> subscribers) {
        return args -> {
            Coordinator coordinator = new Coordinator();
            // Örnek: Tüm subscriber’lar için connect + subscribe yapalım
            for (Subscriber s : subscribers) {
                try {
                    s.connect("SomePlatform", "user", "pass");
                    coordinator.onConnect("SomePlatform", true);

                    s.subscribe("SomePlatform", "USD/EUR");
                    // Koordinator'da onRateAvailable(...), vs. simule edelim
                    coordinator.onRateAvailable("SomePlatform", "USD/EUR", new Rate("USD/EUR", 1.07));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
